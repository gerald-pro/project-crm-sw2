import gql from "graphql-tag";

export const GET_CONTACTS = gql`
    query ($page: Int!, $size: Int!, $filter: String) { 
        getContacts (page: $page, size: $size, filter: $filter) {
            totalCount
            totalPages
            contacts {
                id
                name
                email
                address
                phone
                createdAt
            }
        }
    }
`;