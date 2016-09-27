import os
import datetime
import re
from collections import Counter
from git import Repo
from jira import JIRA

# Directory 'lucene-solr' is the output of the command 'git clone git://git.apache.org/lucene-solr.git'
repo = Repo("C:/Users/shiro/Desktop/lucene-solr/")
lucene_path = "C:/Users/shiro/Desktop/lucene-solr/lucene/core/src/java/org/apache/lucene/"
# 1. to 6.
data_metrics_min_date = datetime.datetime(2013, 1, 1, 0, 0, 0, 0, None)
data_metrics_max_date = datetime.datetime(2013, 12, 31, 23, 59, 0, 0, None)
# 7.
commit_metrics_min_date = datetime.datetime(2011, 1, 1, 0, 0, 0, 0, None)
percentage_threshhold = 0.05  # if proportion of ownership >= 5%  -->  major contributor

lucene_java_file_paths = []  # empty list
# Fill a list with all java files from core lucene-apache
for root, dirs, files in os.walk(lucene_path):
    for file in files:
        if file.endswith(".java"):
            file_path_shortened = (os.path.os.path.join(root, file))
            file_path_shortened = file_path_shortened.replace('\\', '/')  # replace \ with /
            lucene_java_file_paths.append(file_path_shortened)  # add current path to our list

# For every java file
for file_path in lucene_java_file_paths:
    file_name = os.path.basename(file_path)
    file_path_shortened = file_path.replace(lucene_path, '').replace(file_name, '')

    commits_list = list(repo.iter_commits(paths=file_path))  # get commit_iterator object

    # For every commit that affected the current file
    for i, current_commit in enumerate(commits_list):
        previous_commit = commits_list[i - 1]
        current_commit_date = current_commit.committed_datetime.replace(tzinfo=None)

        # Check that commit date is between min and max
        # In this time period we want to compute the columns 1 2 3 4 5 6.1 6.2 6.3 6.4 6.5 6.6 of the assignment
        if data_metrics_min_date <= current_commit_date <= data_metrics_max_date:
            # ----------------------
            #        1st STEP
            # ----------------------
            # Dictionary that gives us the number of lines of code changed, given an author name
            lines_of_author_hashmap = {}

            # We want to git blame this file  before the changes in the current commit (current commit - 1)
            # and get the blame_commits and lines-changed list
            try:
                blame_info = repo.blame(previous_commit.hexsha, file_path)
                for blame_commit, lines_list in blame_info:
                    blame_commit_date = blame_commit.committed_datetime.replace(tzinfo=None)

                    # Quote from assignment: "Please note that you should blame the file before
                    #                         the changes in the current commit (current commit - 1)"
                    # Update our data collection
                    if blame_commit.author.name in lines_of_author_hashmap:
                        lines_of_author_hashmap[blame_commit.author.name] += len(lines_list)
                    else:
                        lines_of_author_hashmap[blame_commit.author.name] = len(lines_list)
            except:
                pass

            line_contributors_total = 0
            line_contributors_minor = 0
            line_contributors_major = 0
            line_contributors_ownership = 0.0
            line_contributors_author = 0
            line_contributors_author_owner = False

            # Calculate sum and max
            total_lines = float(sum(lines_of_author_hashmap.values()))
            max_lines_count = 0
            if len(lines_of_author_hashmap.values()) > 0:
                max_lines_count = max(lines_of_author_hashmap.values())

            # line_contributors_minor, line_contributors_major
            for lines_count in lines_of_author_hashmap.values():
                if lines_count / total_lines > percentage_threshhold:
                    line_contributors_major += 1
                else:
                    line_contributors_minor += 1

            # line_contributors_total
            line_contributors_total = line_contributors_minor + line_contributors_major

            # line_contributors_ownership
            if total_lines > 0:
                line_contributors_ownership = max_lines_count / total_lines

            # line_contributors_author
            if current_commit.author.name in lines_of_author_hashmap:
                line_contributors_author = lines_of_author_hashmap[current_commit.author.name] / total_lines

            # line_contributors_author_owner
            if line_contributors_author == line_contributors_ownership:
                line_contributors_author_owner = True

            # ----------------------
            #        2nd STEP
            # ----------------------
            authors_name_list = []  # i.e list (A,A,A,B,B) contains 3 commits from author A and 2 commits from author B
            # Second loop of commits
            for current_commit_2 in commits_list:
                current_commit_date_2 = current_commit_2.committed_datetime.replace(tzinfo=None)

                # In this time period we want to compute the columns 7.1 7.2 7.3 7.4 7.5 7.6 of the assignment
                if commit_metrics_min_date <= current_commit_date_2 < current_commit_date:
                    authors_name_list.append(current_commit_2.author.name)

            commit_contributors_total = 0
            commit_contributors_minor = 0
            commit_contributors_major = 0
            commit_contributors_ownership = 0
            commit_contributors_author = 0
            commit_contributors_author_owner = False

            frequency_hashmap = Counter(authors_name_list)  # get number (frequency) of commits, given an author_name

            sum_f = float(sum(frequency_hashmap.values()))
            max_f = 0
            if len(frequency_hashmap.values()) > 0:
                max_f = max(frequency_hashmap.values())

            # commit_contributors_minor, commit_contributors_major
            for f in frequency_hashmap.values():
                if f / sum_f > percentage_threshhold:
                    commit_contributors_major += 1  # Major contributor
                else:
                    commit_contributors_minor += 1  # Minor contributor

            # commit_contributors_total
            commit_contributors_total = commit_contributors_minor + commit_contributors_major

            # commit_contributors_ownership
            if sum_f > 0:
                commit_contributors_ownership = max_f / sum_f

            # commit_contributors_author
            if current_commit.author.name in frequency_hashmap:
                commit_contributors_author = frequency_hashmap[current_commit.author.name] / sum_f

            # commit_contributors_author_owner
            if commit_contributors_author == commit_contributors_ownership:
                commit_contributors_author_owner = True

            print current_commit.hexsha + ',' + \
                  file_name + ',' + \
                  file_path_shortened + ',' + \
                  current_commit.author.name + ',' + \
                  str(current_commit_date) + ',' + \
                  str(line_contributors_total) + ',' + \
                  str(line_contributors_minor) + ',' + \
                  str(line_contributors_major) + ',' + \
                  str(line_contributors_ownership) + ',' + \
                  str(line_contributors_author) + ',' + \
                  str(line_contributors_author_owner) + ',' + \
                  str(commit_contributors_total) + ',' + \
                  str(commit_contributors_minor) + ',' + \
                  str(commit_contributors_major) + ',' + \
                  str(commit_contributors_ownership) + ',' + \
                  str(commit_contributors_author) + ',' + \
                  str(commit_contributors_author_owner)
