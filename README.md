### Edit Lambda

No idea about aws lambda ?  [aws official documentation](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html).

### List of lambda functions :

1. CitationCacheCleaner: This lambda present in `com.paperpal.edit.citation package`. At aws lambda service it's name
   is `citation_cache_cleaner`. This lambda runs everyday early morning and cleans the `citation_style_cache` table data
   older than x days.
