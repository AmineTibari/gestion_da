<template>
  <div class="auth-page">
    <!-- Background Effects -->
    <div class="bg-gradient"></div>
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>
    <div class="bg-orb orb-3"></div>

    <!-- Login Card -->
    <div class="auth-card">
      <div class="auth-header">
        <div class="logo">
          <div class="logo-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="url(#grad)" stroke-width="2"/>
              <path d="M2 17l10 5 10-5" stroke="url(#grad)" stroke-width="2"/>
              <path d="M2 12l10 5 10-5" stroke="url(#grad)" stroke-width="2"/>
              <defs>
                <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#06b6d4"/>
                  <stop offset="100%" style="stop-color:#8b5cf6"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
        </div>
        <h1>Bienvenue</h1>
        <p>Connectez-vous à votre espace</p>
      </div>

      <form @submit.prevent="login" class="auth-form">
        <div class="form-group">
          <label>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            Email
          </label>
          <input 
            v-model="email" 
            type="email" 
            placeholder="votre@email.com"
            required 
          />
        </div>

        <div class="form-group">
          <label>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
            Mot de passe
          </label>
          <div class="password-input">
            <input 
              v-model="password" 
              :type="showPassword ? 'text' : 'password'" 
              placeholder="••••••••"
              required 
            />
            <button type="button" class="toggle-password" @click="showPassword = !showPassword">
              <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                <line x1="1" y1="1" x2="23" y2="23"/>
              </svg>
            </button>
          </div>
        </div>

        <button type="submit" class="btn-submit" :disabled="loading">
          <span v-if="!loading">Se connecter</span>
          <span v-else class="loading-spinner"></span>
        </button>

        <p v-if="error" class="error-message">{{ error }}</p>
      </form>

      <div class="auth-footer">
        <p>Pas encore de compte?</p>
        <router-link to="/register" class="link">Créer un compte</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import { useToast } from "@/composables/useToast";

const email = ref("");
const password = ref("");
const error = ref(null);
const loading = ref(false);
const showPassword = ref(false);
const router = useRouter();
const toast = useToast();

const login = async () => {
  error.value = null;
  loading.value = true;

  try {
    const resp = await axios.post("http://localhost:8093/api/auth/login", {
      email: email.value,
      password: password.value,
    });

    if (resp.data) {
      localStorage.setItem("user", JSON.stringify(resp.data));
      toast.success("Connexion réussie!");
      
      if (resp.data.type == "Demandeur") {
        router.push("/demandeur/board");
      } else if (resp.data.type == "ADMINISTRATEUR") {
        router.push("/admin/demandes");
      }
    } else {
      error.value = "Email ou mot de passe incorrect";
      toast.error("Email ou mot de passe incorrect");
    }
  } catch (e) {
    error.value = "Connexion refusée. Vérifiez vos identifiants.";
    toast.error("Connexion refusée. Vérifiez vos identifiants.");
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  position: relative;
  overflow: hidden;
}

/* Background Effects */
.bg-gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #0f172a 100%);
}

.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
  animation: float 20s infinite ease-in-out;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #06b6d4, #0891b2);
  top: -100px;
  left: -100px;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: linear-gradient(135deg, #8b5cf6, #7c3aed);
  bottom: -150px;
  right: -150px;
  animation-delay: -10s;
}

.orb-3 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #ec4899, #db2777);
  top: 50%;
  right: 20%;
  animation-delay: -5s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -30px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

/* Card */
.auth-card {
  position: relative;
  width: 100%;
  max-width: 420px;
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5);
  animation: slideUp 0.6s ease;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Header */
.auth-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-icon {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(139, 92, 246, 0.2));
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.auth-header h1 {
  color: white;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.auth-header p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 15px;
}

/* Form */
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
}

.form-group label svg {
  color: rgba(255, 255, 255, 0.4);
}

.form-group input {
  width: 100%;
  padding: 16px 18px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 15px;
  font-family: inherit;
  transition: all 0.2s;
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.form-group input:focus {
  outline: none;
  border-color: #06b6d4;
  box-shadow: 0 0 0 3px rgba(6, 182, 212, 0.2);
}

.password-input {
  position: relative;
}

.password-input input {
  padding-right: 50px;
}

.toggle-password {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  padding: 0;
}

.toggle-password:hover {
  color: rgba(255, 255, 255, 0.7);
}

/* Submit Button */
.btn-submit {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #06b6d4, #8b5cf6);
  border: none;
  border-radius: 12px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 8px;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(6, 182, 212, 0.4);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Error */
.error-message {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #f87171;
  padding: 12px 16px;
  border-radius: 10px;
  font-size: 14px;
  text-align: center;
}

/* Footer */
.auth-footer {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  text-align: center;
}

.auth-footer p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  margin-bottom: 8px;
}

.auth-footer .link {
  color: #06b6d4;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.auth-footer .link:hover {
  color: #22d3ee;
}

/* Responsive */
@media (max-width: 480px) {
  .auth-card { padding: 32px 24px; border-radius: 20px; }
  .auth-header h1 { font-size: 24px; }
  .logo-icon { width: 60px; height: 60px; }
  .form-group input { padding: 14px; }
  .btn-submit { padding: 14px; }
}
</style>
