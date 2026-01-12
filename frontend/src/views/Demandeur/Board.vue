<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useToast } from "@/composables/useToast";

const router = useRouter();
const user = ref(null);
const demandes = ref([]);
const loading = ref(true);
const showModal = ref(false);
const toast = useToast();

const formData = ref({
  titreDemande: '',
  typeDemande: '',
  description: '',
  fichier: null
});

onMounted(async () => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/login");
    return;
  }
  user.value = JSON.parse(storedUser);
  await fetchDemandes();
});

const fetchDemandes = async () => {
  try {
    const res = await axios.get(`http://localhost:8093/api/demandes/mes-demandes/${user.value.id}`);
    demandes.value = res.data;
  } catch (err) {
    console.error("Erreur chargement demandes", err);
    toast.error("Erreur lors du chargement des demandes");
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateString) => {
  if (!dateString) return "";
  return new Date(dateString).toLocaleDateString("fr-FR");
};

const formatStatus = (status) => {
  const statuses = { SOUMISE: "En attente", EN_TRAITEMENT: "En cours", VALIDEE: "Validée", REJETEE: "Refusée" };
  return statuses[status] || status;
};

const onFileChange = (e) => {
  formData.value.fichier = e.target.files[0];
};

const closeModal = () => {
  showModal.value = false;
  formData.value = { titreDemande: '', typeDemande: '', description: '', fichier: null };
};

const submitDemande = async () => {
  if (!formData.value.titreDemande || !formData.value.typeDemande) return;

  const data = new FormData();
  data.append('titreDemande', formData.value.titreDemande);
  data.append('typeDemande', formData.value.typeDemande);
  data.append('description', formData.value.description);
  data.append('idDemandeur', user.value.id);
  if (formData.value.fichier) {
    data.append('fichier', formData.value.fichier);
  }

  try {
    await axios.post('http://localhost:8093/api/demandes/soumettre', data, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    closeModal();
    toast.success("Demande soumise avec succès!");
    await fetchDemandes();
  } catch (err) {
    console.error("Erreur soumission", err);
    toast.error("Erreur lors de la soumission");
  }
};

const viewDemande = (id) => {
  router.push(`/demandeur/demande/${id}`);
};

const logout = () => {
  localStorage.removeItem("user");
  toast.info("Déconnexion réussie");
  router.push("/login");
};
</script>

<template>
  <div class="demandeur-layout">
    <div class="bg-gradient"></div>
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>

    <header class="main-header">
      <div class="header-content">
        <div class="logo">
          <div class="logo-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="url(#hGrad)" stroke-width="2"/>
              <path d="M2 17l10 5 10-5" stroke="url(#hGrad)" stroke-width="2"/>
              <path d="M2 12l10 5 10-5" stroke="url(#hGrad)" stroke-width="2"/>
              <defs><linearGradient id="hGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#06b6d4"/><stop offset="100%" style="stop-color:#8b5cf6"/>
              </linearGradient></defs>
            </svg>
          </div>
          <span class="logo-text">Mon Espace</span>
        </div>
        <div class="header-right" v-if="user">
          <div class="user-badge">
            <div class="user-avatar">{{ user.nom?.[0] }}</div>
            <span>{{ user.nom }}</span>
          </div>
          <button class="logout-btn" @click="logout">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="content-container">
        <div class="page-header">
          <div>
            <h1>Mes Demandes</h1>
            <p>Suivez l'état de vos demandes administratives</p>
          </div>
          <button class="new-btn" @click="showModal = true">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/>
            </svg>
            Nouvelle demande
          </button>
        </div>

        <div v-if="loading" class="loading-state">
          <div class="spinner"></div><span>Chargement...</span>
        </div>

        <div v-else-if="demandes.length === 0" class="empty-state">
          <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/><line x1="12" y1="18" x2="12" y2="12"/><line x1="9" y1="15" x2="15" y2="15"/>
          </svg>
          <h3>Aucune demande</h3>
          <p>Créez votre première demande pour commencer</p>
          <button class="cta-btn" @click="showModal = true">Créer une demande</button>
        </div>

        <div v-else class="demandes-grid">
          <div v-for="d in demandes" :key="d.id" class="demande-card" @click="viewDemande(d.id)">
            <div class="card-header">
              <span class="ref-badge">{{ d.reference }}</span>
              <span :class="['status-badge', d.status]">{{ formatStatus(d.status) }}</span>
            </div>
            <h3>{{ d.titreDemande }}</h3>
            <p class="type">{{ d.typeDemande }}</p>
            <div class="card-footer">
              <span class="date">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                  <line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
                {{ formatDate(d.dateDepot) }}
              </span>
              <span class="view-link">Voir détails →</span>
            </div>
          </div>
        </div>
      </div>
    </main>

    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h3>Nouvelle demande</h3>
            <button @click="closeModal" class="close-btn">×</button>
          </div>
          <form @submit.prevent="submitDemande" class="modal-body">
            <div class="form-group">
              <label>Titre de la demande</label>
              <input v-model="formData.titreDemande" type="text" placeholder="Ex: Demande de certificat" required />
            </div>
            <div class="form-group">
              <label>Type de demande</label>
              <select v-model="formData.typeDemande" required>
                <option value="">Sélectionner...</option>
                <option value="Certificat">Certificat</option>
                <option value="Attestation">Attestation</option>
                <option value="Autorisation">Autorisation</option>
                <option value="Autre">Autre</option>
              </select>
            </div>
            <div class="form-group">
              <label>Description</label>
              <textarea v-model="formData.description" rows="4" placeholder="Décrivez votre demande..."></textarea>
            </div>
            <div class="form-group">
              <label>Fichier joint (optionnel)</label>
              <div class="file-input">
                <input type="file" id="fileInput" @change="onFileChange" />
                <label for="fileInput" class="file-label">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/>
                  </svg>
                  {{ formData.fichier ? formData.fichier.name : 'Choisir un fichier' }}
                </label>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn-cancel" @click="closeModal">Annuler</button>
              <button type="submit" class="btn-submit">Soumettre</button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.demandeur-layout { min-height: 100vh; display: flex; flex-direction: column; position: relative; }
.bg-gradient { position: fixed; inset: 0; background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%); z-index: -2; }
.bg-orb { position: fixed; border-radius: 50%; filter: blur(100px); opacity: 0.4; z-index: -1; }
.orb-1 { width: 500px; height: 500px; background: linear-gradient(135deg, #06b6d4, #0891b2); top: -200px; left: -100px; }
.orb-2 { width: 600px; height: 600px; background: linear-gradient(135deg, #8b5cf6, #7c3aed); bottom: -200px; right: -200px; }

.main-header { background: rgba(15,23,42,0.8); backdrop-filter: blur(16px); border-bottom: 1px solid rgba(255,255,255,0.08); position: sticky; top: 0; z-index: 100; }
.header-content { max-width: 1200px; margin: 0 auto; padding: 16px 32px; display: flex; justify-content: space-between; align-items: center; }
.logo { display: flex; align-items: center; gap: 12px; }
.logo-icon { width: 44px; height: 44px; background: linear-gradient(135deg, rgba(6,182,212,0.15), rgba(139,92,246,0.15)); border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.logo-text { font-size: 18px; font-weight: 700; background: linear-gradient(135deg, #06b6d4, #8b5cf6); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.header-right { display: flex; align-items: center; gap: 16px; }
.user-badge { display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.05); padding: 8px 16px 8px 8px; border-radius: 12px; }
.user-avatar { width: 32px; height: 32px; background: linear-gradient(135deg, #8b5cf6, #06b6d4); border-radius: 8px; display: flex; align-items: center; justify-content: center; color: white; font-weight: 700; font-size: 14px; }
.user-badge span { color: white; font-size: 14px; font-weight: 500; }
.logout-btn { width: 40px; height: 40px; background: rgba(239,68,68,0.1); border: none; border-radius: 10px; color: #f87171; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all 0.2s; }
.logout-btn:hover { background: rgba(239,68,68,0.2); }

.main-content { flex: 1; padding: 32px; }
.content-container { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; }
.page-header h1 { color: white; font-size: 28px; margin: 0 0 8px; }
.page-header p { color: rgba(255,255,255,0.5); margin: 0; }
.new-btn { display: flex; align-items: center; gap: 8px; padding: 14px 24px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); border: none; border-radius: 14px; color: white; font-weight: 600; font-size: 15px; cursor: pointer; transition: all 0.2s; }
.new-btn:hover { transform: translateY(-2px); box-shadow: 0 10px 30px rgba(6,182,212,0.4); }

.loading-state, .empty-state { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 100px 20px; text-align: center; }
.spinner { width: 48px; height: 48px; border: 3px solid rgba(255,255,255,0.1); border-top-color: #06b6d4; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 16px; }
@keyframes spin { to { transform: rotate(360deg); } }
.loading-state span { color: rgba(255,255,255,0.5); }
.empty-state svg { color: rgba(255,255,255,0.2); margin-bottom: 24px; }
.empty-state h3 { color: white; font-size: 20px; margin: 0 0 8px; }
.empty-state p { color: rgba(255,255,255,0.5); margin: 0 0 24px; }
.cta-btn { padding: 14px 28px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); border: none; border-radius: 12px; color: white; font-weight: 600; cursor: pointer; }

.demandes-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
.demande-card { background: rgba(15,23,42,0.6); backdrop-filter: blur(12px); border: 1px solid rgba(255,255,255,0.08); border-radius: 18px; padding: 24px; transition: transform 0.2s, box-shadow 0.2s; cursor: pointer; }
.demande-card:hover { transform: translateY(-6px); box-shadow: 0 16px 32px rgba(0,0,0,0.3); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.ref-badge { background: rgba(139,92,246,0.15); color: #a78bfa; padding: 6px 12px; border-radius: 8px; font-size: 12px; font-weight: 600; font-family: monospace; }
.status-badge { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.status-badge.SOUMISE { background: rgba(59,130,246,0.15); color: #60a5fa; }
.status-badge.EN_TRAITEMENT { background: rgba(245,158,11,0.15); color: #fbbf24; }
.status-badge.VALIDEE { background: rgba(16,185,129,0.15); color: #34d399; }
.status-badge.REJETEE { background: rgba(239,68,68,0.15); color: #f87171; }
.demande-card h3 { color: white; font-size: 18px; margin: 0 0 8px; }
.demande-card .type { color: rgba(255,255,255,0.5); font-size: 14px; margin: 0 0 20px; }
.card-footer { display: flex; align-items: center; justify-content: space-between; }
.date { display: flex; align-items: center; gap: 6px; color: rgba(255,255,255,0.4); font-size: 13px; }
.view-link { color: #22d3ee; font-size: 13px; font-weight: 500; }

.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.7); backdrop-filter: blur(8px); display: flex; align-items: center; justify-content: center; z-index: 1000; padding: 24px; }
.modal { background: linear-gradient(145deg, rgba(30,41,59,0.98), rgba(15,23,42,0.98)); border: 1px solid rgba(255,255,255,0.1); border-radius: 24px; width: 100%; max-width: 520px; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 24px 28px; border-bottom: 1px solid rgba(255,255,255,0.08); }
.modal-header h3 { color: white; font-size: 20px; margin: 0; }
.close-btn { background: none; border: none; color: rgba(255,255,255,0.5); font-size: 32px; cursor: pointer; line-height: 1; }
.modal-body { padding: 28px; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; color: rgba(255,255,255,0.7); font-size: 14px; font-weight: 500; margin-bottom: 10px; }
.form-group input, .form-group select, .form-group textarea { width: 100%; padding: 14px 16px; background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); border-radius: 12px; color: white; font-size: 15px; font-family: inherit; transition: border-color 0.2s; }
.form-group input:focus, .form-group select:focus, .form-group textarea:focus { outline: none; border-color: #06b6d4; }
.form-group input::placeholder, .form-group textarea::placeholder { color: rgba(255,255,255,0.3); }
.file-input input { display: none; }
.file-label { display: flex; align-items: center; gap: 10px; padding: 14px 16px; background: rgba(255,255,255,0.05); border: 1px dashed rgba(255,255,255,0.2); border-radius: 12px; color: rgba(255,255,255,0.5); cursor: pointer; transition: all 0.2s; }
.file-label:hover { border-color: #06b6d4; color: #22d3ee; }
.modal-footer { display: flex; gap: 12px; justify-content: flex-end; padding-top: 8px; }
.btn-cancel { padding: 14px 24px; background: rgba(255,255,255,0.08); border: none; border-radius: 12px; color: rgba(255,255,255,0.7); font-size: 15px; cursor: pointer; }
.btn-submit { padding: 14px 24px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); border: none; border-radius: 12px; color: white; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>