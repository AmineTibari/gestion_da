<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const demande = ref(null)
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  await fetchDemande()
})

const fetchDemande = async () => {
  try {
    const res = await fetch(`http://localhost:8093/api/demandes/${route.params.id}`)
    if (!res.ok) throw new Error('Demande introuvable')
    demande.value = await res.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('fr-FR', {
    day: 'numeric', month: 'long', year: 'numeric'
  })
}

const formatStatus = (status) => {
  const statuses = { SOUMISE: 'En attente', EN_TRAITEMENT: 'En cours', VALIDEE: 'Validée', REJETEE: 'Refusée' }
  return statuses[status] || status
}

const downloadFile = () => {
  if (demande.value?.nomFichier) {
    window.open(`http://localhost:8093/api/demandes/download/${demande.value.nomFichier}`, '_blank')
  }
}
</script>

<template>
  <div class="demandeur-layout">
    <div class="bg-gradient"></div>
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>

    <header class="main-header">
      <div class="header-content">
        <button class="back-btn" @click="router.push('/demandeur/board')">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/>
          </svg>
          Retour
        </button>
      </div>
    </header>

    <main class="main-content">
      <div class="content-container">
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div><span>Chargement...</span>
        </div>

        <div v-else-if="error" class="error-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/>
          </svg>
          <h3>{{ error }}</h3>
        </div>

        <div v-else class="detail-card">
          <div class="card-header">
            <div>
              <span class="ref-badge">{{ demande.reference }}</span>
              <h1>{{ demande.titreDemande }}</h1>
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
            <div class="info-item" v-if="demande.dateTraitement">
              <label>Date de traitement</label>
              <span>{{ formatDate(demande.dateTraitement) }}</span>
            </div>
            <div class="info-item" v-if="demande.administrateur">
              <label>Traité par</label>
              <span>{{ demande.administrateur.nom }} {{ demande.administrateur.prenom }}</span>
            </div>
          </div>

          <div class="description-section">
            <label>Description</label>
            <p>{{ demande.description || 'Aucune description fournie' }}</p>
          </div>

          <div v-if="demande.nomFichier" class="file-section">
            <label>Fichier joint</label>
            <button class="file-btn" @click="downloadFile">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/>
              </svg>
              Télécharger mon fichier
            </button>
          </div>

          <!-- Status Timeline -->
          <div class="timeline-section">
            <label>Historique</label>
            <div class="timeline">
              <div class="timeline-item done">
                <div class="timeline-dot"></div>
                <div class="timeline-content">
                  <span class="timeline-title">Demande soumise</span>
                  <span class="timeline-date">{{ formatDate(demande.dateDepot) }}</span>
                </div>
              </div>
              <div :class="['timeline-item', { done: demande.status !== 'SOUMISE' }]">
                <div class="timeline-dot"></div>
                <div class="timeline-content">
                  <span class="timeline-title">En cours de traitement</span>
                </div>
              </div>
              <div :class="['timeline-item', { done: demande.status === 'VALIDEE' || demande.status === 'REJETEE' }]">
                <div class="timeline-dot"></div>
                <div class="timeline-content">
                  <span class="timeline-title">{{ demande.status === 'REJETEE' ? 'Demande refusée' : 'Demande validée' }}</span>
                  <span v-if="demande.dateTraitement" class="timeline-date">{{ formatDate(demande.dateTraitement) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.demandeur-layout { min-height: 100vh; display: flex; flex-direction: column; position: relative; }
.bg-gradient { position: fixed; inset: 0; background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%); z-index: -2; }
.bg-orb { position: fixed; border-radius: 50%; filter: blur(100px); opacity: 0.4; z-index: -1; }
.orb-1 { width: 500px; height: 500px; background: linear-gradient(135deg, #06b6d4, #0891b2); top: -200px; left: -100px; }
.orb-2 { width: 600px; height: 600px; background: linear-gradient(135deg, #8b5cf6, #7c3aed); bottom: -200px; right: -200px; }

.main-header { background: rgba(15,23,42,0.8); backdrop-filter: blur(16px); border-bottom: 1px solid rgba(255,255,255,0.08); position: sticky; top: 0; z-index: 100; }
.header-content { max-width: 900px; margin: 0 auto; padding: 16px 32px; }
.back-btn { display: flex; align-items: center; gap: 8px; background: none; border: none; color: rgba(255,255,255,0.7); font-size: 14px; cursor: pointer; padding: 0; transition: color 0.2s; }
.back-btn:hover { color: white; }

.main-content { flex: 1; padding: 32px; }
.content-container { max-width: 900px; margin: 0 auto; }

.loading-state, .error-state { display: flex; flex-direction: column; align-items: center; padding: 80px; color: rgba(255,255,255,0.5); }
.spinner { width: 40px; height: 40px; border: 3px solid rgba(255,255,255,0.1); border-top-color: #06b6d4; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 16px; }
@keyframes spin { to { transform: rotate(360deg); } }
.error-state svg { color: #f87171; margin-bottom: 16px; }
.error-state h3 { color: white; }

.detail-card { background: rgba(15,23,42,0.6); backdrop-filter: blur(12px); border: 1px solid rgba(255,255,255,0.08); border-radius: 20px; padding: 32px; }

.card-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; gap: 20px; flex-wrap: wrap; }
.ref-badge { display: inline-block; background: rgba(139,92,246,0.15); color: #a78bfa; padding: 6px 12px; border-radius: 8px; font-size: 13px; font-weight: 600; font-family: monospace; margin-bottom: 12px; }
.card-header h1 { color: white; font-size: 24px; margin: 0; }
.status-badge { padding: 10px 18px; border-radius: 24px; font-size: 14px; font-weight: 600; white-space: nowrap; }
.status-badge.SOUMISE { background: rgba(59,130,246,0.15); color: #60a5fa; }
.status-badge.EN_TRAITEMENT { background: rgba(245,158,11,0.15); color: #fbbf24; }
.status-badge.VALIDEE { background: rgba(16,185,129,0.15); color: #34d399; }
.status-badge.REJETEE { background: rgba(239,68,68,0.15); color: #f87171; }

.info-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 24px; margin-bottom: 28px; }
.info-item { display: flex; flex-direction: column; gap: 6px; }
.info-item label { color: rgba(255,255,255,0.4); font-size: 12px; font-weight: 500; text-transform: uppercase; }
.info-item span { color: white; font-size: 15px; }

.description-section { background: rgba(255,255,255,0.03); border-radius: 14px; padding: 20px; margin-bottom: 24px; }
.description-section label { display: block; color: rgba(255,255,255,0.4); font-size: 12px; font-weight: 500; text-transform: uppercase; margin-bottom: 10px; }
.description-section p { color: rgba(255,255,255,0.8); font-size: 15px; line-height: 1.7; margin: 0; }

.file-section { margin-bottom: 28px; }
.file-section label { display: block; color: rgba(255,255,255,0.4); font-size: 12px; font-weight: 500; text-transform: uppercase; margin-bottom: 12px; }
.file-btn { display: inline-flex; align-items: center; gap: 10px; padding: 14px 20px; background: rgba(6,182,212,0.15); border: 1px solid rgba(6,182,212,0.3); border-radius: 12px; color: #22d3ee; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.2s; }
.file-btn:hover { background: rgba(6,182,212,0.25); }

.timeline-section label { display: block; color: rgba(255,255,255,0.4); font-size: 12px; font-weight: 500; text-transform: uppercase; margin-bottom: 16px; }
.timeline { display: flex; flex-direction: column; gap: 0; }
.timeline-item { display: flex; gap: 16px; padding-bottom: 24px; position: relative; }
.timeline-item:last-child { padding-bottom: 0; }
.timeline-item::before { content: ''; position: absolute; left: 7px; top: 20px; bottom: 0; width: 2px; background: rgba(255,255,255,0.1); }
.timeline-item:last-child::before { display: none; }
.timeline-dot { width: 16px; height: 16px; border-radius: 50%; background: rgba(255,255,255,0.1); border: 2px solid rgba(255,255,255,0.2); flex-shrink: 0; position: relative; z-index: 1; }
.timeline-item.done .timeline-dot { background: linear-gradient(135deg, #06b6d4, #8b5cf6); border-color: transparent; }
.timeline-item.done::before { background: linear-gradient(180deg, #06b6d4, rgba(255,255,255,0.1)); }
.timeline-content { display: flex; flex-direction: column; }
.timeline-title { color: white; font-size: 15px; font-weight: 500; }
.timeline-item:not(.done) .timeline-title { color: rgba(255,255,255,0.4); }
.timeline-date { color: rgba(255,255,255,0.4); font-size: 13px; margin-top: 4px; }
</style>
