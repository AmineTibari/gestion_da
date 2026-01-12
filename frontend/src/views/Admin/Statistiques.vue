<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const demandes = ref([])
const loading = ref(true)

onMounted(async () => {
  await fetchDemandes()
})

const fetchDemandes = async () => {
  try {
    const res = await axios.get('http://localhost:8093/api/demandes/')
    demandes.value = res.data
  } catch (err) {
    console.error('Error loading demandes', err)
  } finally {
    loading.value = false
  }
}

const totalDemandes = computed(() => demandes.value.length)
const soumises = computed(() => demandes.value.filter(d => d.status === 'SOUMISE').length)
const enTraitement = computed(() => demandes.value.filter(d => d.status === 'EN_TRAITEMENT').length)
const validees = computed(() => demandes.value.filter(d => d.status === 'VALIDEE').length)
const rejetees = computed(() => demandes.value.filter(d => d.status === 'REJETEE').length)

const recentDemandes = computed(() => {
  return [...demandes.value]
    .sort((a, b) => new Date(b.dateDepot) - new Date(a.dateDepot))
    .slice(0, 5)
})

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('fr-FR');
};

const formatStatus = (status) => {
  const statuses = { SOUMISE: 'Nouveau', EN_TRAITEMENT: 'En cours', VALIDEE: 'Validée', REJETEE: 'Refusée' };
  return statuses[status] || status;
};

const soumisesPercent = computed(() => totalDemandes.value ? (soumises.value / totalDemandes.value * 100).toFixed(0) : 0)
const enTraitementPercent = computed(() => totalDemandes.value ? (enTraitement.value / totalDemandes.value * 100).toFixed(0) : 0)
const valideesPercent = computed(() => totalDemandes.value ? (validees.value / totalDemandes.value * 100).toFixed(0) : 0)
const rejeteesPercent = computed(() => totalDemandes.value ? (rejetees.value / totalDemandes.value * 100).toFixed(0) : 0)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1>Statistiques</h1>
      <p>Aperçu des performances et indicateurs clés</p>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <span>Chargement des statistiques...</span>
    </div>

    <div v-else>
      <!-- KPI Cards -->
      <div class="kpi-grid">
        <div class="kpi-card">
          <div class="kpi-icon total">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-value">{{ totalDemandes }}</span>
            <span class="kpi-label">Total Demandes</span>
          </div>
        </div>

        <div class="kpi-card">
          <div class="kpi-icon new">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="16"/>
              <line x1="8" y1="12" x2="16" y2="12"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-value">{{ soumises }}</span>
            <span class="kpi-label">Nouveau</span>
          </div>
        </div>

        <div class="kpi-card">
          <div class="kpi-icon progress">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-value">{{ enTraitement }}</span>
            <span class="kpi-label">En Cours</span>
          </div>
        </div>

        <div class="kpi-card">
          <div class="kpi-icon success">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-value">{{ validees }}</span>
            <span class="kpi-label">Validées</span>
          </div>
        </div>

        <div class="kpi-card">
          <div class="kpi-icon danger">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="15" y1="9" x2="9" y2="15"/>
              <line x1="9" y1="9" x2="15" y2="15"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-value">{{ rejetees }}</span>
            <span class="kpi-label">Refusées</span>
          </div>
        </div>
      </div>

      <!-- Charts -->
      <div class="charts-grid">
        <div class="card">
          <h3>Distribution par Status</h3>
          <div class="bar-chart">
            <div class="bar-item">
              <span class="bar-label">Nouveau</span>
              <div class="bar-track">
                <div class="bar-fill new" :style="{ width: soumisesPercent + '%' }"></div>
              </div>
              <span class="bar-value">{{ soumisesPercent }}%</span>
            </div>
            <div class="bar-item">
              <span class="bar-label">En cours</span>
              <div class="bar-track">
                <div class="bar-fill progress" :style="{ width: enTraitementPercent + '%' }"></div>
              </div>
              <span class="bar-value">{{ enTraitementPercent }}%</span>
            </div>
            <div class="bar-item">
              <span class="bar-label">Validées</span>
              <div class="bar-track">
                <div class="bar-fill success" :style="{ width: valideesPercent + '%' }"></div>
              </div>
              <span class="bar-value">{{ valideesPercent }}%</span>
            </div>
            <div class="bar-item">
              <span class="bar-label">Refusées</span>
              <div class="bar-track">
                <div class="bar-fill danger" :style="{ width: rejeteesPercent + '%' }"></div>
              </div>
              <span class="bar-value">{{ rejeteesPercent }}%</span>
            </div>
          </div>
        </div>

        <div class="card">
          <h3>Demandes Récentes</h3>
          <div class="recent-list">
            <div v-for="d in recentDemandes" :key="d.id" class="recent-item">
              <div class="recent-info">
                <span class="recent-ref">{{ d.reference }}</span>
                <span class="recent-title">{{ d.titreDemande }}</span>
              </div>
              <span :class="['status-dot', d.status]"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 32px;
  max-width: 1400px;
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

.loading-state {
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

/* KPI Cards */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.kpi-card {
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.kpi-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.3);
}

.kpi-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.kpi-icon.total { background: rgba(139, 92, 246, 0.15); color: #a78bfa; }
.kpi-icon.new { background: rgba(59, 130, 246, 0.15); color: #60a5fa; }
.kpi-icon.progress { background: rgba(245, 158, 11, 0.15); color: #fbbf24; }
.kpi-icon.success { background: rgba(16, 185, 129, 0.15); color: #34d399; }
.kpi-icon.danger { background: rgba(239, 68, 68, 0.15); color: #f87171; }

.kpi-content {
  display: flex;
  flex-direction: column;
}

.kpi-value {
  color: white;
  font-size: 28px;
  font-weight: 700;
}

.kpi-label {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
}

/* Charts */
.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.card {
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 24px;
}

.card h3 {
  color: white;
  font-size: 16px;
  margin: 0 0 24px;
}

.bar-chart {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.bar-item {
  display: grid;
  grid-template-columns: 80px 1fr 50px;
  align-items: center;
  gap: 12px;
}

.bar-label {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
}

.bar-track {
  height: 10px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 5px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 5px;
  transition: width 1s ease;
}

.bar-fill.new { background: linear-gradient(90deg, #3b82f6, #60a5fa); }
.bar-fill.progress { background: linear-gradient(90deg, #f59e0b, #fbbf24); }
.bar-fill.success { background: linear-gradient(90deg, #10b981, #34d399); }
.bar-fill.danger { background: linear-gradient(90deg, #ef4444, #f87171); }

.bar-value {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  text-align: right;
}

/* Recent List */
.recent-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recent-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 10px;
}

.recent-info {
  display: flex;
  flex-direction: column;
}

.recent-ref {
  color: #a78bfa;
  font-size: 12px;
  font-family: monospace;
}

.recent-title {
  color: white;
  font-size: 14px;
}

.status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.status-dot.SOUMISE { background: #60a5fa; }
.status-dot.EN_TRAITEMENT { background: #fbbf24; }
.status-dot.VALIDEE { background: #34d399; }
.status-dot.REJETEE { background: #f87171; }

@media (max-width: 900px) {
  .charts-grid { grid-template-columns: 1fr; }
}
</style>
