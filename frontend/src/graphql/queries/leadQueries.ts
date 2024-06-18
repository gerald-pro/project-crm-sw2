import gql from "graphql-tag";

export const GET_LEADS = gql`
    query ($page: Int!, $size: Int!) { 
        getLeads (page: $page, size: $size) {
            totalCount
            totalPages
            leads {
              id
              title
              description
              value
              userId
              contactId
              status
              createdAt
            }
        }
    }
`;

export const GET_LEAD = gql`
    query ($id: String!) { 
        getLead (id: $id) {
            id
            title
            description
            value
            userId
            contactId
            status
            createdAt
        }
    }
`;