<template>
  <div class="login-container">
    <h2>Connexion</h2>

    <form @submit.prevent="login">
      <div class="form-group">
        <label>Email</label>
        <input v-model="email" type="email" required />
      </div>

      <div class="form-group">
        <label>Mot de passe</label>
        <input v-model="password" type="password" required />
      </div>

      <button type="submit">Se connecter</button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const email = ref("");
const password = ref("");
const error = ref(null);
const router = useRouter();

const login = async () => {
  error.value = null;

  try {
    const resp = await axios.post("http://localhost:8093/api/auth/login", {
      email: email.value,
      password: password.value,
    });

    if (resp.data) {
      // Save user session (localStorage or Pinia)
      localStorage.setItem("user", JSON.stringify(resp.data));

      // Redirect after success (example => dashboard)
      console.log(resp.data.type);
      if (resp.data.type == "Demandeur") {
        router.push("/demandeur/board");
      } else if (resp.data.type == "ADMINISTRATEUR") {
        router.push("/admin");
      }
    } else {
      error.value = "Email ou mot de passe incorrect !";
    }
  } catch (e) {
    error.value = "Connexion refusée !";
  }
};
</script>

<style>
.login-container {
  width: 350px;
  margin: 60px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 12px;
}
.error {
  color: red;
}
</style>
