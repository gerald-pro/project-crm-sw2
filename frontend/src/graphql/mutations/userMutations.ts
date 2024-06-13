import gql from "graphql-tag";

export const USER_SIGNUP = gql`
  mutation ($name: String!, $username: String!, $password: String! $email: String!){
    register (name: $name, username: $username, password: $password, email: $email) {
      token
      user {
        id
        name
        username
        email
      }
    }
  }
`;

export const LOGIN = gql`
  mutation ($username: String!, $password: String!) {
    login (username: $username, password: $password) {
      token
      user {
        id
        name
        username
        email
      }
    }
  }

`;