import gql from "graphql-tag";

export const CREATE_CONTACT = gql`
  mutation ($name: String!, $email: String!, $phone: String, $address: String) {
    createContact(input: { name: $name, email: $email, phone: $phone, address: $address }) {
      id
      name
      email
      phone
      address
      createdAt
    }
  }
`;

export const UPDATE_CONTACT = gql`
  mutation ($id: String!, $name: String!, $email: String!, $phone: String!, $address: String!) {
    updateContact(id: $id, input: { name: $name, email: $email, phone: $phone, address: $address }) {
      id
      name
      email
      phone
      address
      createdAt
    }
  }
`;


export const DELETE_CONTACT = gql`
  mutation ($id: ID!) {
    deleteContact(id: $id)
  }
`;