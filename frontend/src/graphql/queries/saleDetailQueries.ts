import gql from "graphql-tag";

export const GET_SALES = gql`
    query ($page: Int!, $size: Int!) { 
        getSalesDetails (page: $page, size: $size) {
            totalCount
            totalPages
            salesDetails {
                invoiceNo
                invoiceDate
                quantity
                country
                description
            }
        }
    }
`;