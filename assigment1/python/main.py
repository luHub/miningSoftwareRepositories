import os
import datetime
import re
from collections import Counter
from git import Repo
from jira import JIRA

# directory 'lucene-solr' is the output of the command 'git clone git://git.apache.org/lucene-solr.git'
repo = Repo("C:/Users/shiro/Desktop/lucene-solr/")
lucene_path = "C:/Users/shiro/Desktop/lucene-solr/lucene/core/src/java/org/apache/lucene/"
commit_min_date = datetime.datetime(2014, 1, 2, 0, 23, 24, 0, None)
commit_max_date = datetime.datetime(2015, 2, 1, 0, 1, 0, 0, None)
bug_min_date = datetime.datetime(2015, 2, 1, 0, 2, 0, 0, None)
bug_max_date = datetime.datetime(2015, 8, 1, 0, 0, 0, 0, None)
percentage_threshhold = 0.05  # if proportion of ownership >= 5%  -->  major contributor
jira = JIRA({'server': 'https://issues.apache.org/jira/'})  # Connect to Jira instance

lucene_java_file_paths = []  # empty list

# Fill a list with all java files from core lucene-apache
for root, dirs, files in os.walk(lucene_path):
    for file in files:
        if file.endswith(".java"):
            path = (os.path.os.path.join(root, file))
            path = path.replace('\\', '/')  # replace \ with /
            lucene_java_file_paths.append(path)  # add current path to our list

# We use a dictionary as a cache to improve the effeciency of our algorithm
# Our dictionary's keys are issue-ids and the value is either True if the issue is a bug or False otherwise
issue_id_bugs_dict = {}

# For every java file
for file_path in lucene_java_file_paths:
    path = file_path.replace(lucene_path, '')
    m = 0
    M = 0
    ownership = 0
    bugs_count = 0

    commits_list = list(repo.iter_commits(paths=file_path))  # get commit_iterator object
    authors_name_list = []  # i.e list (A,A,A,B,B) contains 3 commits from author A and 2 commits from author B

    # For every commit in that file
    for commit in commits_list:
        author_name = commit.author.name
        commit_date = commit.committed_datetime.replace(tzinfo=None)

        # Check that commit date is between min and max
        if commit_min_date < commit_date < commit_max_date:
            authors_name_list.append(author_name)

        # Check that commit date is between min and max
        if bug_min_date < commit_date < bug_max_date:
            # Search for the issue-id inside of the commit message
            find = re.search('LUCENE-(\d+)', commit.message)
            # If we found a issue-id reference in commit message
            if find is not None:
                issue_id = str(find.group(0))
                if issue_id in issue_id_bugs_dict:
                    if issue_id_bugs_dict[issue_id]:
                        bugs_count += 1
                else:
                    issue = jira.issue(issue_id)
                    issuetype_name = str(issue.fields.issuetype.name)
                    # Check if this issue is a bug
                    if issuetype_name.upper() == "BUG":
                        bugs_count += 1
                        issue_id_bugs_dict[issue_id] = True
                    else:
                        issue_id_bugs_dict[issue_id] = False

    c = Counter(authors_name_list)
    frequency_list = c.values()  # get number of commits per author_name

    sum_f = float(sum(frequency_list))

    for f in frequency_list:
        proportion_of_ownership = f / sum_f
        if proportion_of_ownership >= percentage_threshhold:
            M += 1  # Major contributor
        else:
            m += 1  # Minor contributor

    if len(frequency_list) > 0:
        ownership = max(frequency_list) / sum_f

    print path + ',' + \
          str(m) + ',' + \
          str(M) + ',' + \
          str(m + M) + ',' + \
          str(ownership) + ',' + \
          str(bugs_count)
