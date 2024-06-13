// package com.jonatan.dev.crm_sales.config;

// import java.io.File;
// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.io.ClassPathResource;

// import graphql.GraphQL;
// import graphql.kickstart.tools.GraphQLResolver;
// import graphql.schema.GraphQLSchema;
// import graphql.schema.idl.RuntimeWiring;
// import graphql.schema.idl.SchemaGenerator;
// import graphql.schema.idl.SchemaParser;
// import graphql.schema.idl.TypeDefinitionRegistry;

// @Configuration
// public class GraphQLConfig {
//     private final GraphQLResolver itemResolver;
//     private final MutationResolver mutationResolver;

//     @Autowired
//     public GraphQLConfig(GraphQLResolver itemResolver,
//                          MutationResolver mutationResolver) {
//         this.itemResolver = itemResolver;
//         this.mutationResolver = mutationResolver;
//     }

//     @Bean
//     public GraphQL graphQL(RuntimeWiring runtimeWiring) throws IOException {
//         SchemaParser schemaParser = new SchemaParser();
//         TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();

//         // Load the GraphQL schema from the schema.graphqls file
//         String schemaFile = new ClassPathResource("graphql/user.graphqls").getFile().getAbsolutePath();
//         typeDefinitionRegistry.merge(schemaParser.parse(new File(schemaFile)));

//         SchemaGenerator schemaGenerator = new SchemaGenerator();
//         GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(
//                 typeDefinitionRegistry, runtimeWiring
//         );

//         return GraphQL.newGraphQL(graphQLSchema).build();
//     }

//     @Bean
//     RuntimeWiring runtimeWiring() {
//         return RuntimeWiring.newRuntimeWiring()
//                 .type("Query", builder -> builder
//                                 .dataFetcher("findAll", this.itemResolver.getAllItemsDataFetcher())
//                                 // Add more mutation resolvers as needed
//                 )
//                 .type("Mutation", builder -> builder
//                                 .dataFetcher("create", this.mutationResolver.createDataFetcher())
//                                 // Add more mutation resolvers as needed
//                 )
//                 .build();
//     }
// }