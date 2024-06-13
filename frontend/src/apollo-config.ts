import {
    ApolloClient,
    createHttpLink,
    InMemoryCache,
  } from "@apollo/client/core";
import { createApolloProvider } from "@vue/apollo-option";
  
  // HTTP connection to the API
  const httpLink = createHttpLink({
    uri: "http://127.0.0.1:9091/graphql", // Matches the url and port that Django is using
  });
  
  // Cache implementation
  const cache = new InMemoryCache();
  
  // Create the apollo client
  export const apolloClient = new ApolloClient({
    link: httpLink,
    cache: cache,
  });


  export const apolloProvider = createApolloProvider({
    defaultClient: apolloClient,
  })