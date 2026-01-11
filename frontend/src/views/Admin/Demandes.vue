<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

// Liste des demandes
const demandes = ref([]);

// État de chargement et erreur
const loading = ref(true);
const error = ref(null);

// Filtre par status (ex: "EN_ATTENTE", "VALIDE", "REJETE")
const statusFilter = ref("");

// Fonction pour charger toutes les demandes
const fetchDemandes = async () => {
  loading.value = true;
  error.value = null;
  try {
    let url = "http://localhost:8093/api/demandes/";
    if (statusFilter.value) {
      url = `http://localhost:8093/api/demandes/filtrer?status=${statusFilter.value}`;
    }
    const res = await axios.get(url);
    demandes.value = res.data;
    console.log(demandes.value.id);
  } catch (err) {
    error.value = "Erreur lors du chargement des demandes.";
    console.error(err);
  } finally {
    loading.value = false;
  }
};

// Recharger les demandes au montage du component
onMounted(() => {
  fetchDemandes();
});

// Fonctions pour changer le status et filtrer
const handleFilterChange = () => {
  fetchDemandes();
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0'); // الشهور تبدأ من 0
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
};

const formatStatus = (status) => {
  if (status == "SOUMISE") {
    return "Nouveau";
  } else if (status == "EN_TRAITEMENT") {
    return "En cours";
  } else if (status == "VALIDEE") {
    return "Validee";
  } else if (status == "REJETEE") {
    return "Refusee";
  }
}

const goToDemande = (id) => {
  router.push(`/demandes/${id}`);
}


</script>

<template>
    <div class="demandes-container">

      <div class="header">
        <h2>Gestion des demandes</h2>

        <div class="filter">
          <label>Status :</label>
          <select v-model="statusFilter" @change="handleFilterChange">
            <option value="">Tous</option>
            <option value="SOUMISE">Nouveau</option>
            <option value="EN_TRAITEMENT">En cours</option>
            <option value="VALIDEE">Validée</option>
            <option value="REJETEE">Refusée</option>
          </select>
        </div>
      </div>

      <div v-if="loading" class="loading">Chargement...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <table v-else-if="demandes.length">
        <thead>
        <tr>
          <th>Référence</th>
          <th>Demandeur</th>
          <th>Type</th>
          <th>Status</th>
          <th>Date Dépôt</th>
          <th>Administrateur</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="demande in demandes" :key="demande.id">
          <td>{{ demande.reference }}</td>
          <td>{{ demande.demandeur?.nom }} {{ demande.demandeur?.prenom }}</td>
          <td>{{ demande.titreDemande }}</td>

          <td>
          <span :class="['status-badge', demande.status]">
            {{ formatStatus(demande.status) }}
          </span>
          </td>

          <td>{{ formatDate(demande.dateDepot) }}</td>

          <td>
          <span v-if="demande.administrateur">
            {{ demande.administrateur.nom }}
          </span>
            <span class="badge-unassigned" v-else>
            Non assignée
          </span>
          </td>
          <td class="voir" >
            <FontAwesomeIcon :icon="['fas', 'eye']" style="color: #1e3a8a;" @click="() => goToDemande(demande.id)" />
          </td>
        </tr>
        </tbody>
      </table>

      <div v-if="!loading && !demandes.length" class="no-results">
        Aucune demande trouvée.
      </div>

    </div>
</template>

<style scoped>

* {
  font-family: "Poppins", sans-serif;
}

.header h2 {
  font-family: "Poppins", sans-serif;
  font-weight: 600;
}



.demandes-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header .filter label {
  margin-right: 10px;
}
.header .filter select {
  width: 140px;
}

.filter select {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #888;
  outline: none;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

th {
  background: #1e3a8a;
  color: white;
  padding: 15px 10px;
}

td {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

th, td {
  text-align: center;
}

.voir {
  cursor: pointer;
}

.loading {
  font-size: 15px;
}

.no-results {
  padding: 10px;
  color: #777;
}

/* Badges */
.status-badge {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.status-badge.SOUMISE {
  background: #3b82f6;  /* Bleu */
}

.status-badge.EN_TRAITEMENT {
  background: #f59e0b; /* Orange */
}

.status-badge.VALIDEE {
  background: #10b981; /* Vert */
}

.status-badge.REJETEE {
  background: #ef4444; /* Rouge */
}

.badge-unassigned {
  padding: 3px 6px;
  border-radius: 6px;
  background: #9ca3af;
  color: white;
  font-size: 12px;
}

</style>
