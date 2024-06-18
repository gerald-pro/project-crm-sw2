import gql from "graphql-tag";

export const GET_ACTIVITIES_BY_LEAD = gql`
    query ($id: String!) {
        getActivitiesByLead(id: $id) {
            id
            title
            description
            type
            location
            activityDate
            leadId
        }
    }
`;
