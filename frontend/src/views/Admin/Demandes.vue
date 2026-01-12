<script setup>
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();
const demandes = ref([]);
const loading = ref(true);
const searchQuery = ref("");
const statusFilter = ref("all");

onMounted(async () => {
  await fetchDemandes();
});

const fetchDemandes = async () => {
  try {
    const res = await axios.get("http://localhost:8093/api/demandes/");
    demandes.value = res.data;
  } catch (err) {
    console.error("Error loading demandes", err);
  } finally {
    loading.value = false;
  }
};

const filteredDemandes = computed(() => {
  let result = demandes.value;
  
  if (statusFilter.value !== "all") {
    result = result.filter(d => d.status === statusFilter.value);
  }
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(d => 
      d.reference?.toLowerCase().includes(query) ||
      d.titreDemande?.toLowerCase().includes(query) ||
      d.demandeur?.nom?.toLowerCase().includes(query)
    );
  }
  
  return result;
});

const statusCounts = computed(() => ({
  all: demandes.value.length,
  SOUMISE: demandes.value.filter(d => d.status === 'SOUMISE').length,
  EN_TRAITEMENT: demandes.value.filter(d => d.status === 'EN_TRAITEMENT').length,
  VALIDEE: demandes.value.filter(d => d.status === 'VALIDEE').length,
  REJETEE: demandes.value.filter(d => d.status === 'REJETEE').length,
}));

const formatDate = (dateString) => {
  if (!dateString) return "";
  return new Date(dateString).toLocaleDateString("fr-FR");
};

const formatStatus = (status) => {
  const statuses = {
    SOUMISE: "Nouveau",
    EN_TRAITEMENT: "En cours",
    VALIDEE: "Validée",
    REJETEE: "Refusée"
  };
  return statuses[status] || status;
};

const goToDemande = (id) => {
  router.push(`/admin/demandes/${id}`);
};
</script>

<template>
  <div class="page-container">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <h1>Demandes</h1>
        <p>Gérez les demandes administratives</p>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-section">
      <div class="search-box">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="Rechercher par référence, titre ou demandeur..."
        />
      </div>

      <div class="status-tabs">
        <button 
          :class="['tab', { active: statusFilter === 'all' }]"
          @click="statusFilter = 'all'">
          Tous <span class="count">{{ statusCounts.all }}</span>
        </button>
        <button 
          :class="['tab', 'tab-new', { active: statusFilter === 'SOUMISE' }]"
          @click="statusFilter = 'SOUMISE'">
          Nouveau <span class="count">{{ statusCounts.SOUMISE }}</span>
        </button>
        <button 
          :class="['tab', 'tab-progress', { active: statusFilter === 'EN_TRAITEMENT' }]"
          @click="statusFilter = 'EN_TRAITEMENT'">
          En cours <span class="count">{{ statusCounts.EN_TRAITEMENT }}</span>
        </button>
        <button 
          :class="['tab', 'tab-done', { active: statusFilter === 'VALIDEE' }]"
          @click="statusFilter = 'VALIDEE'">
          Validées <span class="count">{{ statusCounts.VALIDEE }}</span>
        </button>
        <button 
          :class="['tab', 'tab-rejected', { active: statusFilter === 'REJETEE' }]"
          @click="statusFilter = 'REJETEE'">
          Refusées <span class="count">{{ statusCounts.REJETEE }}</span>
        </button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <span>Chargement des demandes...</span>
    </div>

    <!-- Empty State -->
    <div v-else-if="filteredDemandes.length === 0" class="empty-state">
      <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
        <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/>
      </svg>
      <h3>Aucune demande trouvée</h3>
      <p>Essayez de modifier vos filtres de recherche</p>
    </div>

    <!-- Table -->
    <div v-else class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Référence</th>
            <th>Titre</th>
            <th>Demandeur</th>
            <th>Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="d in filteredDemandes" :key="d.id" @click="goToDemande(d.id)">
            <td class="ref-cell">
              <span class="ref-badge">{{ d.reference }}</span>
            </td>
            <td>{{ d.titreDemande }}</td>
            <td class="demandeur-cell">
              <div class="demandeur-info">
                <div class="avatar">{{ d.demandeur?.nom?.[0] }}</div>
                <span>{{ d.demandeur?.nom }} {{ d.demandeur?.prenom }}</span>
              </div>
            </td>
            <td>{{ formatDate(d.dateDepot) }}</td>
            <td>
              <span :class="['status-badge', d.status]">
                {{ formatStatus(d.status) }}
              </span>
            </td>
            <td>
              <button class="action-btn" @click.stop="goToDemande(d.id)">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 32px;
  max-width: 1400px;
  margin: 0 auto;
}

/* Header */
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
  font-size: 15px;
  margin: 0;
}

/* Filters */
.filters-section {
  margin-bottom: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 14px 18px;
  max-width: 500px;
}

.search-box svg {
  color: rgba(255, 255, 255, 0.4);
  flex-shrink: 0;
}

.search-box input {
  flex: 1;
  background: none;
  border: none;
  color: white;
  font-size: 15px;
  font-family: inherit;
}

.search-box input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.search-box input:focus {
  outline: none;
}

.status-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.tab:hover {
  background: rgba(255, 255, 255, 0.08);
  color: white;
}

.tab.active {
  background: rgba(6, 182, 212, 0.15);
  border-color: rgba(6, 182, 212, 0.3);
  color: #22d3ee;
}

.tab .count {
  background: rgba(255, 255, 255, 0.1);
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
}

.tab.active .count {
  background: rgba(6, 182, 212, 0.3);
}

/* Loading & Empty */
.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
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

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-state svg {
  margin-bottom: 20px;
  opacity: 0.3;
}

.empty-state h3 {
  color: white;
  font-size: 18px;
  margin: 0 0 8px;
}

.empty-state p {
  margin: 0;
}

/* Table */
.table-container {
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  text-align: left;
  padding: 16px 20px;
  background: rgba(255, 255, 255, 0.03);
  color: rgba(255, 255, 255, 0.5);
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.data-table td {
  padding: 16px 20px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.data-table tbody tr {
  cursor: pointer;
  transition: background 0.2s;
}

.data-table tbody tr:hover {
  background: rgba(255, 255, 255, 0.03);
}

.ref-badge {
  background: rgba(139, 92, 246, 0.15);
  color: #a78bfa;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  font-family: monospace;
}

.demandeur-cell .demandeur-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.demandeur-cell .avatar {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #8b5cf6, #06b6d4);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 13px;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.status-badge.SOUMISE {
  background: rgba(59, 130, 246, 0.15);
  color: #60a5fa;
}

.status-badge.EN_TRAITEMENT {
  background: rgba(245, 158, 11, 0.15);
  color: #fbbf24;
}

.status-badge.VALIDEE {
  background: rgba(16, 185, 129, 0.15);
  color: #34d399;
}

.status-badge.REJETEE {
  background: rgba(239, 68, 68, 0.15);
  color: #f87171;
}

.action-btn {
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.05);
  border: none;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.action-btn:hover {
  background: rgba(6, 182, 212, 0.15);
  color: #22d3ee;
}

/* Responsive */
@media (max-width: 900px) {
  .page-container { padding: 20px; }
  .status-tabs { flex-wrap: wrap; }
  .data-table { font-size: 13px; }
  .data-table th, .data-table td { padding: 12px 10px; }
  .demandeur-cell .demandeur-info span { display: none; }
}

@media (max-width: 600px) {
  .page-header h1 { font-size: 22px; }
  .table-container { overflow-x: auto; }
  .data-table { min-width: 600px; }
}
</style>
