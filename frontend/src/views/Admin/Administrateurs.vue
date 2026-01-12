<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useToast } from "@/composables/useToast"

const admins = ref([])
const loading = ref(true)
const showModal = ref(false)
const toast = useToast()

const formData = ref({
  nom: '', prenom: '', email: '', password: '', cni: '', role: 'ADMIN', titreFonction: ''
})

onMounted(() => { fetchAdmins() })

const fetchAdmins = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8093/api/admin/administrateurs/')
    admins.value = res.data
  } catch (err) { 
    console.error('Error', err)
    toast.error('Erreur lors du chargement')
  }
  finally { loading.value = false }
}

const closeModal = () => {
  showModal.value = false
  formData.value = { nom: '', prenom: '', email: '', password: '', cni: '', role: 'ADMIN', titreFonction: '' }
}

const submitAdmin = async () => {
  if (!formData.value.nom || !formData.value.email || !formData.value.password) return
  try {
    await axios.post('http://localhost:8093/api/admin/administrateurs', {
      nom: formData.value.nom, prenom: formData.value.prenom, email: formData.value.email,
      password: formData.value.password, cni: formData.value.cni, type: 'ADMINISTRATEUR',
      role: formData.value.role, titreFonction: formData.value.titreFonction
    })
    closeModal()
    toast.success('Administrateur créé avec succès')
    fetchAdmins()
  } catch (err) { 
    console.error('Error', err)
    toast.error('Erreur lors de la création')
  }
}

const deleteAdmin = async (id) => {
  try {
    await axios.delete(`http://localhost:8093/api/admin/administrateurs/${id}`)
    toast.success('Administrateur supprimé')
    fetchAdmins()
  } catch (err) { 
    console.error('Error', err)
    toast.error('Erreur lors de la suppression')
  }
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1>Administrateurs</h1>
        <p>Gestion des comptes administrateurs</p>
      </div>
      <button class="add-btn" @click="showModal = true">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/>
        </svg>
        Nouvel administrateur
      </button>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="spinner"></div><span>Chargement...</span>
    </div>

    <div v-else-if="admins.length === 0" class="empty-state">
      <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/>
        <path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>
      </svg>
      <h3>Aucun administrateur</h3>
    </div>

    <div v-else class="admin-grid">
      <div v-for="admin in admins" :key="admin.id" class="admin-card">
        <div class="card-header">
          <div class="avatar">{{ admin.nom?.[0] }}</div>
          <div class="admin-info">
            <h3>{{ admin.nom }} {{ admin.prenom }}</h3>
            <span class="role">{{ admin.role || 'ADMIN' }}</span>
          </div>
          <button class="delete-btn" @click="deleteAdmin(admin.id)">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="3 6 5 6 21 6"/>
              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
            </svg>
          </button>
        </div>
        <div class="card-body">
          <div class="info-row">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            <span>{{ admin.email }}</span>
          </div>
          <div class="info-row" v-if="admin.titreFonction">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
            </svg>
            <span>{{ admin.titreFonction }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h3>Nouvel administrateur</h3>
            <button @click="closeModal" class="close-btn">×</button>
          </div>
          <form @submit.prevent="submitAdmin" class="modal-body">
            <div class="form-row">
              <div class="form-group"><label>Nom</label><input v-model="formData.nom" required /></div>
              <div class="form-group"><label>Prénom</label><input v-model="formData.prenom" required /></div>
            </div>
            <div class="form-group"><label>Email</label><input v-model="formData.email" type="email" required /></div>
            <div class="form-group"><label>Mot de passe</label><input v-model="formData.password" type="password" required /></div>
            <div class="form-row">
              <div class="form-group"><label>CNI</label><input v-model="formData.cni" /></div>
              <div class="form-group">
                <label>Rôle</label>
                <select v-model="formData.role">
                  <option value="ADMIN">Admin</option>
                  <option value="SUPER_ADMIN">Super Admin</option>
                </select>
              </div>
            </div>
            <div class="form-group"><label>Fonction</label><input v-model="formData.titreFonction" /></div>
            <div class="modal-footer">
              <button type="button" class="btn-cancel" @click="closeModal">Annuler</button>
              <button type="submit" class="btn-submit">Créer</button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.page-container { padding: 32px; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; }
.page-header h1 { color: white; font-size: 28px; margin: 0 0 4px; }
.page-header p { color: rgba(255,255,255,0.5); margin: 0; }

.add-btn {
  display: flex; align-items: center; gap: 8px; padding: 12px 20px;
  background: linear-gradient(135deg, #8b5cf6, #06b6d4); border: none; border-radius: 12px;
  color: white; font-weight: 600; cursor: pointer; transition: all 0.2s;
}
.add-btn:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(139,92,246,0.4); }

.loading-state, .empty-state { display: flex; flex-direction: column; align-items: center; padding: 80px; color: rgba(255,255,255,0.5); }
.spinner { width: 40px; height: 40px; border: 3px solid rgba(255,255,255,0.1); border-top-color: #06b6d4; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 16px; }
@keyframes spin { to { transform: rotate(360deg); } }
.empty-state svg { margin-bottom: 16px; opacity: 0.3; }
.empty-state h3 { color: white; margin: 0; }

.admin-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }

.admin-card {
  background: rgba(15,23,42,0.6); backdrop-filter: blur(12px);
  border: 1px solid rgba(255,255,255,0.08); border-radius: 16px;
  overflow: hidden; transition: transform 0.2s, box-shadow 0.2s;
}
.admin-card:hover { transform: translateY(-4px); box-shadow: 0 12px 24px rgba(0,0,0,0.3); }

.card-header { display: flex; align-items: center; gap: 14px; padding: 20px; background: rgba(255,255,255,0.03); }
.avatar { width: 48px; height: 48px; background: linear-gradient(135deg, #8b5cf6, #06b6d4); border-radius: 12px; display: flex; align-items: center; justify-content: center; color: white; font-weight: 700; font-size: 18px; }
.admin-info { flex: 1; }
.admin-info h3 { color: white; font-size: 16px; margin: 0 0 4px; }
.role { color: #a78bfa; font-size: 12px; font-weight: 600; }
.delete-btn { width: 36px; height: 36px; background: rgba(239,68,68,0.1); border: none; border-radius: 10px; color: #f87171; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all 0.2s; }
.delete-btn:hover { background: rgba(239,68,68,0.2); }

.card-body { padding: 16px 20px; }
.info-row { display: flex; align-items: center; gap: 10px; color: rgba(255,255,255,0.6); font-size: 14px; margin-bottom: 10px; }
.info-row svg { color: rgba(255,255,255,0.4); }

/* Modal */
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.7); backdrop-filter: blur(8px); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: linear-gradient(145deg, rgba(30,41,59,0.98), rgba(15,23,42,0.98)); border: 1px solid rgba(255,255,255,0.1); border-radius: 20px; width: 90%; max-width: 500px; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 20px 24px; border-bottom: 1px solid rgba(255,255,255,0.08); }
.modal-header h3 { color: white; margin: 0; }
.close-btn { background: none; border: none; color: rgba(255,255,255,0.5); font-size: 28px; cursor: pointer; }
.modal-body { padding: 24px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; color: rgba(255,255,255,0.7); font-size: 14px; margin-bottom: 8px; }
.form-group input, .form-group select { width: 100%; padding: 12px; background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); border-radius: 10px; color: white; font-size: 14px; font-family: inherit; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: #8b5cf6; }
.modal-footer { display: flex; gap: 12px; justify-content: flex-end; padding-top: 8px; }
.btn-cancel { padding: 12px 20px; background: rgba(255,255,255,0.08); border: none; border-radius: 10px; color: rgba(255,255,255,0.7); cursor: pointer; }
.btn-submit { padding: 12px 20px; background: linear-gradient(135deg, #8b5cf6, #06b6d4); border: none; border-radius: 10px; color: white; font-weight: 600; cursor: pointer; }
</style>