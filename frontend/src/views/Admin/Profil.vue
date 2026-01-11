<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faIdCard, faEnvelope, faUser, faUserTie, faKey } from '@fortawesome/free-solid-svg-icons';

const router = useRouter();
const user = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) return router.push("/login");
  user.value = JSON.parse(storedUser);
});

const logout = () => {
  localStorage.removeItem("user");
  router.push("/login");
};
</script>

<template>
  <div class="profil-admin" v-if="user">
    <div class="profile-card">
      <!-- Header -->
      <div class="profile-header">
        <div class="avatar">{{ user.nom?.charAt(0) }}</div>
        <div class="user-basic">
          <h2>{{ user.nom }} {{ user.prenom }}</h2>
          <p style="margin-bottom: 8px">{{ user.email }}</p>
          <span class="role">{{ user.role }}</span>
        </div>
      </div>

      <!-- Info -->
      <div class="profile-info">
        <div class="info-item">
          <FontAwesomeIcon :icon="faIdCard" class="info-icon"/>
          <span class="info-label">CNI</span>
          <span class="info-value">{{ user.cni }}</span>
        </div>
        <div class="info-item">
          <FontAwesomeIcon :icon="faUserTie" class="info-icon"/>
          <span class="info-label">Fonction</span>
          <span class="info-value">{{ user.titreFonction }}</span>
        </div>
        <div class="info-item">
          <FontAwesomeIcon :icon="faKey" class="info-icon"/>
          <span class="info-label">Mot de passe</span>
          <span class="info-value">••••••••</span>
        </div>
      </div>

      <!-- Logout -->
      <div class="profile-actions">
        <button @click="logout">Se Déconnecter</button>
      </div>
    </div>
  </div>

  <div v-else>
    <p>Chargement des informations...</p>
  </div>
</template>

<style scoped>

* {
  font-family: "Poppins", sans-serif;
}

.profil-admin {
  padding: 32px;
  display: flex;
  justify-content: center;
}

.profile-card {
  width: 100%;
  max-width: 500px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease;
}


.profile-header {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 24px;
  display: flex;
  gap: 16px;
  align-items: center;
}

.avatar {
  width: 80px;
  height: 80px;
  background: rgba(255,255,255,0.3);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 32px;
  font-weight: bold;
}

.user-basic h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
}
.user-basic p {
  margin: 4px 0;
  font-size: 14px;
}
.user-basic .role {
  display: inline-block;
  background: rgba(255,255,255,0.3);
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.profile-info {
  padding: 16px 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f9fafb;
  padding: 12px 16px;
  border-radius: 12px;
  transition: background 0.3s ease;
}
.info-item:hover {
  background: #eff6ff;
}
.info-icon {
  color: #667eea;
  font-size: 20px;
  min-width: 28px;
}
.info-label {
  font-size: 12px;
  font-weight: 600;
  color: #6b7280;
  flex: 1;
}
.info-value {
  font-size: 14px;
  font-weight: 600;
  color: #1f2937;
}

.profile-actions {
  padding: 16px 24px;
  display: flex;
  justify-content: center;
}
.profile-actions button {
  padding: 10px 24px;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}
.profile-actions button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102,126,234,0.3);
}
</style>
