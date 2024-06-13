// package com.jonatan.dev.crm_sales.config;

// import graphql.GraphQLError;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.util.List;
// import java.util.stream.Collectors;

// @Configuration
// public class GraphQLErrorHandlerConfig {

//     @Bean
//     public GraphQLErrorHandler errorHandler() {
//         return new GraphQLErrorHandler() {
//             @Override
//             public List<GraphQLError> processErrors(List<GraphQLError> errors) {
//                 return errors.stream()
//                         .map(this::getNested)
//                         .collect(Collectors.toList());
//             }

//             private GraphQLError getNested(GraphQLError error) {
//                 if (error.getExtensions() != null && error.getExtensions().containsKey("exception")) {
//                     Throwable exception = (Throwable) error.getExtensions().get("exception");
//                     if (exception.getCause() != null) {
//                         return new ThrowableGraphQLError(exception.getCause());
//                     }
//                 }
//                 return error;
//             }
//         };
//     }
// }
