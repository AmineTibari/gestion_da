<template>
  <div class="register-container">
    <h2>Créer un compte</h2>

    <form @submit.prevent="register">
      <div class="form-group">
        <label>Nom</label>
        <input v-model="nom" type="text" required />
      </div>

      <div class="form-group">
        <label>Prénom</label>
        <input v-model="prenom" type="text" required />
      </div>

      <div class="form-group">
        <label>Email</label>
        <input v-model="email" type="email" required />
      </div>

      <div class="form-group">
        <label>Mot de passe</label>
        <input v-model="password" type="password" required />
      </div>

      <div class="form-group">
        <label>CNI</label>
        <input v-model="cni" type="text" />
      </div>


      <button type="submit">S'inscrire</button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="success" class="success">{{ success }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const nom = ref("");
const prenom = ref("");
const email = ref("");
const password = ref("");
const cni = ref("");

const error = ref(null);
const success = ref(null);

const register = async () => {
  error.value = null;
  success.value = null;

  try {
    const resp = await axios.post(
        "http://localhost:8093/api/auth/register",
        {
          nom: nom.value,
          prenom: prenom.value,
          email: email.value,
          password: password.value,
          cni: cni.value,
        }
    );

    if (resp.data) {
      success.value = "Compte créé avec succès !";
      setTimeout(() => router.push("/login"), 1500);
    } else {
      error.value = "Erreur lors de la création du compte.";
    }
  } catch (e) {
    error.value = e.response?.data?.message || "Erreur serveur !";
  }
};
</script>

<style scoped>
.register-container {
  width: 400px;
  margin: 60px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
}
.form-group {
  margin-bottom: 12px;
}
.error {
  color: red;
  margin-top: 10px;
}
.success {
  color: green;
  margin-top: 10px;
}
button {
  margin-top: 12px;
  padding: 8px 16px;
  cursor: pointer;
}
</style>
