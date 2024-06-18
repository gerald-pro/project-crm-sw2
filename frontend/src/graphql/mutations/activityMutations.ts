import gql from "graphql-tag";

export const CREATE_ACTIVITY = gql`
  mutation ($title: String!, $description: String, $type: ActivityType!, $location: String, $activityDate: String!, $leadId: String!) {
    createActivity(input: { title: $title, description: $description, type: $type, location: $location, activityDate: $activityDate, leadId: $leadId} )  {
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

export const UPDATE_ACTIVITY = gql`
  mutation ($id: String!, $title: String, $description: String, $type: ActivityType, $location: String, $activityDate: String) {
    updateActivity (input: { id: $id, title: $title, description: $description, type: $type, location: $location, activityDate: $activityDate} )  {
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

export const DELETE_ACTIVITY = gql`
  mutation ($id: String!) {
    deleteActivity (id: $id)
  }
`;