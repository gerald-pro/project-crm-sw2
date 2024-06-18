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

export const GET_USERS = gql`
    query ($page: Int, $size: Int, $filter: String) { 
        getUsers (page: $page, size: $size, filter: $filter) {
            totalCount
            totalPages
            users {
                id
                name
                username
                email
                createdAt
            }
        }
    }
`;


/**
 * Obtiene un usuario por su ID.
 */
export const GET_USER = gql`
    query ($id: String!) { 
        getUser(id: $id) {
        id
        name
        username
        password
        }
    }
`;