<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useToast } from "@/composables/useToast"

const user = ref(null)
const activeTab = ref('received')
const receivedMessages = ref([])
const sentMessages = ref([])
const allUsers = ref([])
const loading = ref(true)
const showComposeModal = ref(false)
const selectedMessage = ref(null)
const toast = useToast()

const formData = ref({
  receiverId: '',
  subject: '',
  content: ''
})

onMounted(async () => {
  const storedUser = localStorage.getItem("user")
  if (storedUser) {
    user.value = JSON.parse(storedUser)
    await Promise.all([
      fetchReceivedMessages(),
      fetchSentMessages(),
      fetchAllUsers()
    ])
  }
})

const fetchReceivedMessages = async () => {
  try {
    const res = await axios.get(`http://localhost:8093/api/messages/received/${user.value.id}`)
    receivedMessages.value = res.data
  } catch (err) {
    console.error('Error', err)
  } finally { loading.value = false }
}

const fetchSentMessages = async () => {
  try {
    const res = await axios.get(`http://localhost:8093/api/messages/sent/${user.value.id}`)
    sentMessages.value = res.data
  } catch (err) { console.error('Error', err) }
}

const fetchAllUsers = async () => {
  try {
    const res = await axios.get('http://localhost:8093/api/admin/administrateurs/')
    allUsers.value = res.data.filter(u => u.id !== user.value.id)
  } catch (err) { console.error('Error', err) }
}

const messages = computed(() => activeTab.value === 'received' ? receivedMessages.value : sentMessages.value)
const unreadCount = computed(() => receivedMessages.value.filter(m => !m.isRead).length)

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  if (diff < 86400000) return date.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
  return date.toLocaleDateString('fr-FR')
}

const closeComposeModal = () => { showComposeModal.value = false; formData.value = { receiverId: '', subject: '', content: '' } }

const sendMessage = async () => {
  if (!formData.value.receiverId || !formData.value.subject) return
  try {
    await axios.post('http://localhost:8093/api/messages/send', {
      senderId: user.value.id,
      receiverId: formData.value.receiverId,
      subject: formData.value.subject,
      content: formData.value.content
    })
    closeComposeModal()
    toast.success('Message envoyé avec succès')
    await fetchSentMessages()
  } catch (err) { 
    console.error('Error', err)
    toast.error('Erreur lors de l\'envoi du message')
  }
}

const openMessage = async (msg) => {
  selectedMessage.value = msg
  if (activeTab.value === 'received' && !msg.isRead) {
    await axios.put(`http://localhost:8093/api/messages/read/${msg.id}`)
    await fetchReceivedMessages()
  }
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1>Messagerie</h1>
        <p>Communication interne</p>
      </div>
      <button class="compose-btn" @click="showComposeModal = true">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        Nouveau message
      </button>
    </div>

    <div class="content-grid">
      <!-- Sidebar -->
      <div class="mail-sidebar">
        <div class="tabs">
          <button :class="['tab', { active: activeTab === 'received' }]" @click="activeTab = 'received'">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 12h-6l-2 3h-4l-2-3H2"/>
              <path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"/>
            </svg>
            Reçus
            <span v-if="unreadCount" class="badge">{{ unreadCount }}</span>
          </button>
          <button :class="['tab', { active: activeTab === 'sent' }]" @click="activeTab = 'sent'">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/>
            </svg>
            Envoyés
          </button>
        </div>

        <div v-if="loading" class="loading">Chargement...</div>
        <div v-else-if="messages.length === 0" class="empty">Aucun message</div>
        <div v-else class="message-list">
          <div 
            v-for="msg in messages" 
            :key="msg.id"
            :class="['message-item', { unread: activeTab === 'received' && !msg.isRead, selected: selectedMessage?.id === msg.id }]"
            @click="openMessage(msg)">
            <div class="msg-avatar">{{ (activeTab === 'received' ? msg.sender : msg.receiver)?.nom?.[0] }}</div>
            <div class="msg-content">
              <div class="msg-header">
                <span class="msg-sender">{{ (activeTab === 'received' ? msg.sender : msg.receiver)?.nom }}</span>
                <span class="msg-date">{{ formatDate(msg.dateEnvoi) }}</span>
              </div>
              <div class="msg-subject">{{ msg.subject }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Message View -->
      <div class="mail-content">
        <div v-if="!selectedMessage" class="no-selection">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
            <polyline points="22,6 12,13 2,6"/>
          </svg>
          <p>Sélectionnez un message</p>
        </div>
        <div v-else class="message-view">
          <div class="view-header">
            <h2>{{ selectedMessage.subject }}</h2>
            <span class="view-date">{{ formatDate(selectedMessage.dateEnvoi) }}</span>
          </div>
          <div class="view-sender">
            <div class="sender-avatar">{{ (activeTab === 'received' ? selectedMessage.sender : selectedMessage.receiver)?.nom?.[0] }}</div>
            <div class="sender-info">
              <span class="sender-name">{{ (activeTab === 'received' ? selectedMessage.sender : selectedMessage.receiver)?.nom }}</span>
              <span class="sender-email">{{ (activeTab === 'received' ? selectedMessage.sender : selectedMessage.receiver)?.email }}</span>
            </div>
          </div>
          <div class="view-body">{{ selectedMessage.content || 'Pas de contenu' }}</div>
        </div>
      </div>
    </div>

    <!-- Compose Modal -->
    <Teleport to="body">
      <div v-if="showComposeModal" class="modal-overlay" @click="closeComposeModal">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h3>Nouveau message</h3>
            <button @click="closeComposeModal" class="close-btn">×</button>
          </div>
          <form @submit.prevent="sendMessage" class="modal-body">
            <div class="form-group">
              <label>Destinataire</label>
              <select v-model="formData.receiverId" required>
                <option value="">Sélectionner...</option>
                <option v-for="u in allUsers" :key="u.id" :value="u.id">{{ u.nom }} {{ u.prenom }}</option>
              </select>
            </div>
            <div class="form-group">
              <label>Sujet</label>
              <input v-model="formData.subject" type="text" required />
            </div>
            <div class="form-group">
              <label>Message</label>
              <textarea v-model="formData.content" rows="6"></textarea>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn-cancel" @click="closeComposeModal">Annuler</button>
              <button type="submit" class="btn-send">Envoyer</button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.page-container { padding: 32px; height: calc(100vh - 64px); display: flex; flex-direction: column; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-header h1 { color: white; font-size: 28px; margin: 0 0 4px; }
.page-header p { color: rgba(255,255,255,0.5); margin: 0; }

.compose-btn {
  display: flex; align-items: center; gap: 8px; padding: 12px 20px;
  background: linear-gradient(135deg, #06b6d4, #8b5cf6); border: none; border-radius: 12px;
  color: white; font-weight: 600; cursor: pointer; transition: all 0.2s;
}
.compose-btn:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(6,182,212,0.4); }

.content-grid { flex: 1; display: grid; grid-template-columns: 340px 1fr; gap: 24px; min-height: 0; }

.mail-sidebar {
  background: rgba(15,23,42,0.6); backdrop-filter: blur(12px);
  border: 1px solid rgba(255,255,255,0.08); border-radius: 16px;
  display: flex; flex-direction: column; overflow: hidden;
}

.tabs { display: flex; border-bottom: 1px solid rgba(255,255,255,0.08); }
.tab {
  flex: 1; display: flex; align-items: center; justify-content: center; gap: 8px;
  padding: 16px; background: none; border: none; color: rgba(255,255,255,0.5);
  font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.2s;
}
.tab:hover { color: white; background: rgba(255,255,255,0.03); }
.tab.active { color: #22d3ee; background: rgba(6,182,212,0.1); border-bottom: 2px solid #06b6d4; }
.badge { background: #ef4444; color: white; padding: 2px 6px; border-radius: 10px; font-size: 11px; }

.message-list { flex: 1; overflow-y: auto; }
.message-item {
  display: flex; gap: 12px; padding: 16px; cursor: pointer;
  border-bottom: 1px solid rgba(255,255,255,0.05); transition: background 0.2s;
}
.message-item:hover { background: rgba(255,255,255,0.03); }
.message-item.selected { background: rgba(6,182,212,0.1); }
.message-item.unread { background: rgba(59,130,246,0.08); }
.message-item.unread .msg-subject { color: white; font-weight: 600; }

.msg-avatar {
  width: 40px; height: 40px; background: linear-gradient(135deg, #8b5cf6, #06b6d4);
  border-radius: 10px; display: flex; align-items: center; justify-content: center;
  color: white; font-weight: 700; font-size: 14px; flex-shrink: 0;
}
.msg-content { flex: 1; min-width: 0; }
.msg-header { display: flex; justify-content: space-between; margin-bottom: 4px; }
.msg-sender { color: white; font-size: 14px; font-weight: 500; }
.msg-date { color: rgba(255,255,255,0.4); font-size: 12px; }
.msg-subject { color: rgba(255,255,255,0.6); font-size: 13px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

.mail-content {
  background: rgba(15,23,42,0.6); backdrop-filter: blur(12px);
  border: 1px solid rgba(255,255,255,0.08); border-radius: 16px;
  display: flex; flex-direction: column; overflow: hidden;
}

.no-selection { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; color: rgba(255,255,255,0.3); }
.no-selection svg { margin-bottom: 16px; }

.message-view { flex: 1; padding: 24px; overflow-y: auto; }
.view-header { display: flex; justify-content: space-between; align-items: start; margin-bottom: 20px; }
.view-header h2 { color: white; font-size: 20px; margin: 0; }
.view-date { color: rgba(255,255,255,0.4); font-size: 13px; }
.view-sender { display: flex; gap: 12px; padding-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.08); margin-bottom: 20px; }
.sender-avatar { width: 44px; height: 44px; background: linear-gradient(135deg, #8b5cf6, #06b6d4); border-radius: 12px; display: flex; align-items: center; justify-content: center; color: white; font-weight: 700; }
.sender-info { display: flex; flex-direction: column; }
.sender-name { color: white; font-weight: 600; }
.sender-email { color: rgba(255,255,255,0.5); font-size: 13px; }
.view-body { color: rgba(255,255,255,0.8); line-height: 1.7; }

.loading, .empty { padding: 40px; text-align: center; color: rgba(255,255,255,0.4); }

/* Modal */
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.7); backdrop-filter: blur(8px); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: linear-gradient(145deg, rgba(30,41,59,0.98), rgba(15,23,42,0.98)); border: 1px solid rgba(255,255,255,0.1); border-radius: 20px; width: 90%; max-width: 500px; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 20px 24px; border-bottom: 1px solid rgba(255,255,255,0.08); }
.modal-header h3 { color: white; margin: 0; }
.close-btn { background: none; border: none; color: rgba(255,255,255,0.5); font-size: 28px; cursor: pointer; }
.modal-body { padding: 24px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; color: rgba(255,255,255,0.7); font-size: 14px; margin-bottom: 8px; }
.form-group input, .form-group select, .form-group textarea {
  width: 100%; padding: 12px; background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1); border-radius: 10px;
  color: white; font-size: 14px; font-family: inherit;
}
.form-group input:focus, .form-group select:focus, .form-group textarea:focus { outline: none; border-color: #06b6d4; }
.modal-footer { display: flex; gap: 12px; justify-content: flex-end; padding-top: 8px; }
.btn-cancel { padding: 12px 20px; background: rgba(255,255,255,0.08); border: none; border-radius: 10px; color: rgba(255,255,255,0.7); cursor: pointer; }
.btn-send { padding: 12px 20px; background: linear-gradient(135deg, #06b6d4, #8b5cf6); border: none; border-radius: 10px; color: white; font-weight: 600; cursor: pointer; }

/* Responsive */
@media (max-width: 900px) {
  .content-grid { grid-template-columns: 1fr; }
  .page-container { padding: 20px; height: auto; }
  .mail-sidebar { max-height: 300px; }
  .mail-content { min-height: 400px; }
}

@media (max-width: 600px) {
  .page-header { flex-direction: column; align-items: flex-start; gap: 16px; }
  .page-header h1 { font-size: 22px; }
  .compose-btn { width: 100%; justify-content: center; }
}
</style>
