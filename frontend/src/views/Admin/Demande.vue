<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const demande = ref(null)
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const res = await fetch(`http://localhost:8093/api/demandes/${route.params.id}`)
    if (!res.ok) throw new Error('Demande introuvable')

    demande.value = await res.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="demande-details">

    <div v-if="loading">Chargement...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else>
      <h2>Demande #{{ demande.id }}</h2>

      <p><strong>Nom:</strong> {{ demande.nom }}</p>
      <p><strong>Email:</strong> {{ demande.email }}</p>
      <p><strong>Status:</strong> {{ demande.status }}</p>
      <p><strong>Date création:</strong> {{ demande.dateCreation }}</p>

      <hr>

      <button @click="$router.back()">Retour</button>
    </div>
  </div>
</template>
