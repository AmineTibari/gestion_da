import { createRouter, createWebHistory } from "vue-router";
import Home from "./views/Home.vue";
import Login from "./views/Login.vue";
import Register from "@/views/Register.vue";
import AdminBoard from "@/views/Admin/AdminBoard.vue";
import Board from "@/views/Demandeur/Board.vue";
import DemandeurDemande from "@/views/Demandeur/Demande.vue";
import Demandes from "@/views/Admin/Demandes.vue";
import Profil from "@/views/Admin/Profil.vue";
import Administrateurs from "@/views/Admin/Administrateurs.vue";
import Demande from "@/views/Admin/Demande.vue";
import Statistiques from "@/views/Admin/Statistiques.vue";
import Messagerie from "@/views/Admin/Messagerie.vue";

const routes = [
    { path: "/", component: Home },
    { path: "/login", component: Login },
    { path: "/register", component: Register },

    {
        path: "/admin",
        component: AdminBoard,
        children: [
            { path: "", redirect: "demandes" },
            { path: "demandes", component: Demandes },
            { path: "messagerie", component: Messagerie },
            { path: "statistiques", component: Statistiques },
            { path: "profil", component: Profil },
            { path: "administrateurs", component: Administrateurs },
            { path: "demandes/:id", component: Demande }
        ]
    },

    { path: "/demandeur/board", component: Board },
    { path: "/demandeur/demande/:id", component: DemandeurDemande },

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
