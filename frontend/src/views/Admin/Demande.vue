<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useToast } from "@/composables/useToast"

const route = useRoute()
const router = useRouter()
const demande = ref(null)
const loading = ref(true)
const error = ref(null)
const admin = ref(null)
const actionLoading = ref(false)
const toast = useToast()

onMounted(async () => {
  const storedUser = localStorage.getItem("user");
  if (storedUser) {
    admin.value = JSON.parse(storedUser);
  }
  await fetchDemande();
})

const fetchDemande = async () => {
  try {
    const res = await fetch(`http://localhost:8093/api/demandes/${route.params.id}`)
    if (!res.ok) throw new Error('Demande introuvable')
    demande.value = await res.json()
  } catch (e) {
    error.value = e.message
    toast.error('Demande introuvable')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
};

const formatStatus = (status) => {
  const statuses = {
    SOUMISE: 'Nouveau',
    EN_TRAITEMENT: 'En cours',
    VALIDEE: 'Validée',
    REJETEE: 'Refusée'
  };
  return statuses[status] || status;
};

const assignToMe = async () => {
  if (!admin.value || !demande.value) return;
  actionLoading.value = true;
  try {
    await axios.put(`http://localhost:8093/api/demandes/assign/${demande.value.id}/${admin.value.id}`);
    toast.success('Demande assignée avec succès');
    await fetchDemande();
  } catch (err) {
    console.error(err);
    toast.error('Erreur lors de l\'assignation');
  } finally {
    actionLoading.value = false;
  }
};

const valider = async () => {
  if (!demande.value) return;
  actionLoading.value = true;
  try {
    await axios.put(`http://localhost:8093/api/demandes/valider/${demande.value.id}`);
    toast.success('Demande validée avec succès');
    await fetchDemande();
  } catch (err) {
    console.error(err);
    toast.error('Erreur lors de la validation');
  } finally {
    actionLoading.value = false;
  }
};

const rejeter = async () => {
  if (!demande.value) return;
  actionLoading.value = true;
  try {
    await axios.put(`http://localhost:8093/api/demandes/rejeter/${demande.value.id}`);
    toast.warning('Demande rejetée');
    await fetchDemande();
  } catch (err) {
    console.error(err);
    toast.error('Erreur lors du rejet');
  } finally {
    actionLoading.value = false;
  }
};

const downloadFile = () => {
  if (demande.value?.nomFichier) {
    window.open(`http://localhost:8093/api/demandes/download/${demande.value.nomFichier}`, '_blank');
  }
};
</script>

<template>
  <div class="page-container">
    <!-- Back Button -->
    <button class="back-btn" @click="router.back()">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <line x1="19" y1="12" x2="5" y2="12"/>
        <polyline points="12 19 5 12 12 5"/>
      </svg>
      Retour aux demandes
    </button>

    <!-- Loading -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <span>Chargement...</span>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="error-state">
      <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"/>
        <line x1="15" y1="9" x2="9" y2="15"/>
        <line x1="9" y1="9" x2="15" y2="15"/>
      </svg>
      <h3>{{ error }}</h3>
    </div>

    <!-- Content -->
    <div v-else class="content-grid">
      <!-- Main Info Card -->
      <div class="card main-card">
        <div class="card-header">
          <div class="header-left">
            <span class="ref-badge">{{ demande.reference }}</span>
            <h2>{{ demande.titreDemande }}</h2>
          </div>
          <span :class="['status-badge', demande.status]">{{ formatStatus(demande.status) }}</span>
        </div>

        <div class="info-grid">
          <div class="info-item">
            <label>Type de demande</label>
            <span>{{ demande.typeDemande }}</span>
          </div>
          <div class="info-item">
            <label>Date de dépôt</label>
            <span>{{ formatDate(demande.dateDepot) }}</span>
          </div>
        </div>

        <div class="description-section">
          <label>Description</label>
          <p>{{ demande.description || 'Aucune description fournie' }}</p>
        </div>

        <!-- File -->
        <div v-if="demande.nomFichier" class="file-section">
          <label>Fichier joint</label>
          <button class="file-btn" @click="downloadFile">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
              <polyline points="7 10 12 15 17 10"/>
              <line x1="12" y1="15" x2="12" y2="3"/>
            </svg>
            Télécharger le fichier
          </button>
        </div>
      </div>

      <!-- Sidebar -->
      <div class="sidebar-cards">
        <!-- Demandeur Card -->
        <div class="card">
          <h3>Demandeur</h3>
          <div class="user-profile">
            <div class="avatar">{{ demande.demandeur?.nom?.[0] }}</div>
            <div class="user-info">
              <span class="name">{{ demande.demandeur?.nom }} {{ demande.demandeur?.prenom }}</span>
              <span class="email">{{ demande.demandeur?.email }}</span>
            </div>
          </div>
          <div class="info-item small">
            <label>CNI</label>
            <span>{{ demande.demandeur?.cni }}</span>
          </div>
        </div>

        <!-- Assignment Card -->
        <div class="card">
          <h3>Assignation</h3>
          <div v-if="demande.administrateur" class="user-profile">
            <div class="avatar admin">{{ demande.administrateur?.nom?.[0] }}</div>
            <div class="user-info">
              <span class="name">{{ demande.administrateur?.nom }}</span>
              <span class="email">Assigné</span>
            </div>
          </div>
          <div v-else class="not-assigned">
            <p>Non assignée</p>
            <button class="assign-btn" @click="assignToMe" :disabled="actionLoading">
              M'assigner cette demande
            </button>
          </div>
        </div>

        <!-- Actions Card -->
        <div class="card actions-card">
          <h3>Actions</h3>
          <div class="action-buttons">
            <button 
              class="btn btn-success" 
              @click="valider"
              :disabled="demande.status === 'VALIDEE' || actionLoading">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              Valider
            </button>
            <button 
              class="btn btn-danger" 
              @click="rejeter"
              :disabled="demande.status === 'REJETEE' || actionLoading">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
              Rejeter
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  cursor: pointer;
  margin-bottom: 24px;
  padding: 0;
  transition: color 0.2s;
}

.back-btn:hover {
  color: white;
}

.loading-state, .error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px;
  color: rgba(255, 255, 255, 0.5);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top-color: #06b6d4;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin { to { transform: rotate(360deg); } }

.error-state svg { color: #f87171; margin-bottom: 16px; }
.error-state h3 { color: white; }

/* Grid */
.content-grid {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 24px;
}

/* Cards */
.card {
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 24px;
}

.card h3 {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin: 0 0 16px;
}

.main-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 28px;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ref-badge {
  background: rgba(139, 92, 246, 0.15);
  color: #a78bfa;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  font-family: monospace;
  width: fit-content;
}

.main-card h2 {
  color: white;
  font-size: 22px;
  margin: 0;
}

.status-badge {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
}

.status-badge.SOUMISE { background: rgba(59, 130, 246, 0.15); color: #60a5fa; }
.status-badge.EN_TRAITEMENT { background: rgba(245, 158, 11, 0.15); color: #fbbf24; }
.status-badge.VALIDEE { background: rgba(16, 185, 129, 0.15); color: #34d399; }
.status-badge.REJETEE { background: rgba(239, 68, 68, 0.15); color: #f87171; }

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-item label {
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
}

.info-item span {
  color: white;
  font-size: 15px;
}

.info-item.small { margin-top: 16px; }

.description-section {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}

.description-section label {
  display: block;
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  margin-bottom: 8px;
}

.description-section p {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

.file-section label {
  display: block;
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  margin-bottom: 12px;
}

.file-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 18px;
  background: rgba(6, 182, 212, 0.15);
  border: 1px solid rgba(6, 182, 212, 0.3);
  border-radius: 10px;
  color: #22d3ee;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.file-btn:hover {
  background: rgba(6, 182, 212, 0.25);
}

/* Sidebar */
.sidebar-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #8b5cf6, #06b6d4);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 16px;
}

.avatar.admin {
  background: linear-gradient(135deg, #10b981, #06b6d4);
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-info .name {
  color: white;
  font-weight: 600;
}

.user-info .email {
  color: rgba(255, 255, 255, 0.5);
  font-size: 13px;
}

.not-assigned {
  text-align: center;
}

.not-assigned p {
  color: rgba(255, 255, 255, 0.4);
  margin: 0 0 12px;
}

.assign-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  border: none;
  border-radius: 10px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.assign-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(59, 130, 246, 0.4);
}

.assign-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  border: none;
  border-radius: 10px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-success {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
}

.btn-success:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4);
}

.btn-danger {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.4);
}

@media (max-width: 900px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
}
</style>
