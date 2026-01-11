import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "./views/Dashboard.vue";
import Login from "./views/Login.vue";
import Register from "@/views/Register.vue";
import AdminBoard from "@/views/Admin/AdminBoard.vue";
import Board from "@/views/Demandeur/Board.vue";
import Demandes from "@/views/Admin/Demandes.vue";
import Profil from "@/views/Admin/Profil.vue";
import Administrateurs from "@/views/Admin/Administrateurs.vue";
import Demande from "@/views/Admin/Demande.vue";

const routes = [
    { path: "/login", component: Login },
    { path: "/dashboard", component: Dashboard },
    { path: "/register", component: Register },

    {
        path: "/admin",
        component: AdminBoard,
        children: [
            { path: "demandes", component: Demandes},
            { path: "profil", component: Profil},
            { path: "administrateurs", component: Administrateurs},
            { path: "demandes/:id", component: Demande }
        ]
    },


    { path: "/demandeur/board", component: Board},

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
