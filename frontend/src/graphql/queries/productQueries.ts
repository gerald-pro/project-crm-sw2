import gql from "graphql-tag";

export const GET_PRODUCTS = gql`
    query ($page: Int!, $size: Int!) { 
        getProducts (page: $page, size: $size) {
            totalCount
            totalPages
            products {
                id
                description
            }
        }
    }
`;