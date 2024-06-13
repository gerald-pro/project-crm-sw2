import gql from "graphql-tag";

export const CREATE_LEAD = gql`
  mutation ($title: String!, $description: String, $value: Float!, $userId: String!, $contactId: String!, $status: String!) {
    createLead(input: { title: $title, description: $description, value: $value, userId: $userId, contactId: $contactId, status: $status }) {
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

export const UPDATE_LEAD = gql`
  mutation ($id: String!, $title: String!, $description: String, $value: Float!, $userId: String!, $contactId: String!, $status: String!) {
    updateLead(id: $id, input: { title: $title, description: $description, value: $value, userId: $userId, contactId: $contactId, status: $status }) {
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


export const DELETE_LEAD = gql`
  mutation ($id: String!) {
    deleteLead(id: $id)
  }
`;