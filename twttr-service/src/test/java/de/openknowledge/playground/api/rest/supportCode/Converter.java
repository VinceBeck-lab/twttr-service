package de.openknowledge.playground.api.rest.supportCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import de.openknowledge.playground.api.rest.security.application.tweet.DetailedTweet;
import de.openknowledge.playground.api.rest.supportCode.AccountCredentials;
import de.openknowledge.playground.api.rest.supportCode.domain.*;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Converter implements TypeRegistryConfigurer{
    private SharedDomain domain = new SharedDomain();

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(NewTweet.class,
                (Map<String, String> row)-> new NewTweet(row.get("value"))));


        typeRegistry.defineDataTableType(new DataTableType (TweetDTO.class,
                (String s)-> new ObjectMapper().readValue(s, TweetDTO.class)));


        typeRegistry.defineDataTableType(new DataTableType (DetailedTweet.class,
                (String s)-> new ObjectMapper().readValue(s, DetailedTweet.class)));


        typeRegistry.defineDataTableType(new DataTableType (ErrorMessage.class,
                (String s)-> new ObjectMapper().readValue(s, ErrorMessage.class)));

        typeRegistry.defineDataTableType(new DataTableType (AccountEntity.class,
                (Map<String, String> row)-> {
                    Integer id = Integer.parseInt(row.get("accountId"));
                    String userName = row.get("userName");
                    String firstName = row.get("firstName");
                    String lastName = row.get("lastName");
                    String roleAsString = row.get("role");
                    if (roleAsString != null && (roleAsString.equals("USER") || roleAsString.equals("MODERATOR"))) {
                        Integer role = roleAsString.equals("USER") ? 0 : 1;
                        String accountType = role.equals(0) ? "USER" : role.equals(1) ? "MODERATOR" : null;
                        return new AccountEntity(accountType, id, userName, firstName, lastName, role);
                    }else {
                        return new AccountEntity(null, id, userName, firstName, lastName, null);
                    }
                }));

        typeRegistry.defineDataTableType(new DataTableType (TweetEntity.class,
                (Map<String, String> row)-> {
                    Integer id = Integer.parseInt(row.get("tweetId"));
                    String content = row.get("content");
                    String stateAsString = row.get("state");
                    Integer state = stateAsString.equals("PUBLISH") ? 0 : null;
                    state = stateAsString.equals("CANCELED") ? 1 : state;
                    String authorName = row.get("author");
                    Integer authorId = authorName != null ? accountIdToUserName(authorName) : null;
                    return new TweetEntity(id, content, null, state, authorId);
                }));

        typeRegistry.defineDataTableType(new DataTableType (GetTweetsQueryParams.class,
                (Map<String, String> row)-> {
                    String numTweets = row.get("numTweets").equals("not setted") ? null : row.get("numTweets");
                    String index = row.get("index").equals("not setted") ? null : row.get("index");
                    return new GetTweetsQueryParams(index, numTweets);
                }));

        typeRegistry.defineDataTableType(new DataTableType (GetUsersQueryParams.class,
                (Map<String, String> row)-> {
                    String s = row.get("searchString");
                    String serachString = null;
                    if (s != null) { serachString = row.get("searchString").equals("not setted")  ? null : row.get("searchString"); }

                    String numTweets = row.get("numTweets").equals("not setted") ? null : row.get("numTweets");
                    String index = row.get("index").equals("not setted") ? null : row.get("index");
                    return new GetUsersQueryParams(serachString, numTweets, index);
                }));

        typeRegistry.defineParameterType(new ParameterType<List>(
                "Ids",
                "([0-9]{0,2}(,[0-9])*)",
                List.class,
                (String s) -> {

                    List<Integer> list = new LinkedList<>();
                    if (s!=null) {
                        String[] stringList = s.split(",");
                        for (int i=0; i<stringList.length; i++) {
                            if (!stringList[i].equals("")) {
                                list.add(Integer.parseInt(stringList[i]));
                            }
                        }
                    }
                    return list;
                }
        ));

        typeRegistry.defineDataTableType(new DataTableType(TweetEntity.Builder.class,
                (Map<String, String> row) ->{
                    Integer tweetId = Integer.parseInt(row.get("tweetId"));
                    String content = row.get("content");
                    String stateAsString = row.get("state");
                    Integer state = stateAsString.equals("PUBLISH") ? 0 : null;
                    state = stateAsString.equals("CANCELED") ? 1 : state;
                    String authorName = row.get("author");
                    Integer authorId = authorName != null ? accountIdToUserName(authorName) : null;
                    String rootTweetAsString = row.get("rootTweetId");
                    Integer rootTweetId = rootTweetAsString == null ? null : Integer.parseInt(rootTweetAsString) ;

                    TweetEntity.Builder builder = TweetEntity.builderInstance();
                    builder.withAuthorId(authorId);
                    builder.withContent(content);
                    builder.withRootTweetId(rootTweetId);
                    builder.withState(state);
                    builder.withPubDate(null);
                    builder.withTweetId(tweetId);
                    return builder;
                }
        ));
    }

    public Integer accountIdToUserName(String userName){
        AccountCredentials accountCredentials = domain.getAccount(userName);
        if (accountCredentials != null) {
            return accountCredentials.getAccountId();
        }else {
            throw new IllegalArgumentException("No account for userName \"" + userName + "\"");
        }
    }
}