// Este archivo contiene consultas GraphQL para obtener datos de usuarios.

import gql from "graphql-tag";

/**
 * Obtiene todos los usuarios registrados.
 */
export const ALL_USERS = gql`
    query {
        getAllUsers {
            id
            name
            username
            email
            createdAt
        }
    }
`;


/**
 * Obtiene un usuario por su ID.
 */
export const USER_BY_ID = gql`
    query ($id: ID!) { 
        getUser(id: $id) {
        id
        name
        username
        password
        roles
        }
    }
`;