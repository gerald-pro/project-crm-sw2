import { defineStore } from "pinia";

// Define las interfaces para el estado
interface UserState {
    token: string | null;
    user: string | null;
}

// Define la estructura del usuario (ajusta según sea necesario)
interface User {
    id: number;
    name: string;
    username: string;
    email: string;
}

export const useUserStore = defineStore({
    id: "user",
    state: (): UserState => ({
        token: localStorage.getItem("token") || null,
        user: localStorage.getItem("user") || null,
    }),
    getters: {
        getToken: (state) => state.token,
        getUser: (state: UserState): User | null => {
            return state.user ? JSON.parse(state.user) : null;
        },
    },
    actions: {
        setToken(token: string) {
            this.token = token;

            // Guarda el token en el almacenamiento local
            localStorage.setItem("token", token);
        },
        removeToken() {
            this.token = null;

            // Elimina el token del almacenamiento local
            localStorage.removeItem("token");
        },
        setUser(user: any) {
            this.user = JSON.stringify(user);

            // Guarda el usuario en el almacenamiento local
            localStorage.setItem("user", this.user);
        },
        removeUser() {
            this.user = null;

            // Elimina el usuario del almacenamiento local
            localStorage.removeItem("user");
        },
    },
});