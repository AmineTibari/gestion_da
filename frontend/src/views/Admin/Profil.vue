<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";

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

const userInitials = computed(() => {
  if (!user.value) return "";
  return (user.value.nom?.[0] || "") + (user.value.prenom?.[0] || "");
});
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1>Mon Profil</h1>
      <p>Gérez vos informations personnelles</p>
    </div>

    <div v-if="user" class="profile-content">
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar-large">{{ userInitials }}</div>
          <div class="profile-name">
            <h2>{{ user.nom }} {{ user.prenom }}</h2>
            <span class="role-badge">{{ user.role || 'Administrateur' }}</span>
          </div>
        </div>

        <div class="profile-details">
          <div class="detail-item">
            <div class="detail-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
            </div>
            <div class="detail-content">
              <label>Email</label>
              <span>{{ user.email }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                <line x1="16" y1="2" x2="16" y2="6"/>
                <line x1="8" y1="2" x2="8" y2="6"/>
                <line x1="3" y1="10" x2="21" y2="10"/>
              </svg>
            </div>
            <div class="detail-content">
              <label>CNI</label>
              <span>{{ user.cni || 'Non renseigné' }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
            </div>
            <div class="detail-content">
              <label>Fonction</label>
              <span>{{ user.titreFonction || 'Non renseigné' }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
            </div>
            <div class="detail-content">
              <label>Mot de passe</label>
              <span>••••••••</span>
            </div>
          </div>
        </div>

        <div class="profile-actions">
          <button class="logout-btn" @click="logout">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
            Se déconnecter
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 32px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h1 {
  color: white;
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px;
}

.page-header p {
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
}

.profile-card {
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  overflow: hidden;
}

.profile-header {
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(139, 92, 246, 0.2));
  padding: 32px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-large {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #06b6d4, #8b5cf6);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  font-weight: 700;
}

.profile-name h2 {
  color: white;
  font-size: 24px;
  margin: 0 0 8px;
}

.role-badge {
  display: inline-block;
  background: rgba(255, 255, 255, 0.15);
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
}

.profile-details {
  padding: 24px 32px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 14px;
  margin-bottom: 12px;
  transition: background 0.2s;
}

.detail-item:hover {
  background: rgba(255, 255, 255, 0.06);
}

.detail-icon {
  width: 44px;
  height: 44px;
  background: rgba(6, 182, 212, 0.15);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #22d3ee;
}

.detail-content {
  display: flex;
  flex-direction: column;
}

.detail-content label {
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  margin-bottom: 4px;
}

.detail-content span {
  color: white;
  font-size: 15px;
}

.profile-actions {
  padding: 24px 32px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  padding: 14px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 12px;
  color: #f87171;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  border-color: rgba(239, 68, 68, 0.4);
}
</style>
