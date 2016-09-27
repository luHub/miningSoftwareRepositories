import os


class GitFileReader:
    pass


def file_reader(self,path):
    lucene_java_file_paths = []  # empty list
    # Fill a list with all java files from core lucene-apache
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(".java"):
                path = (os.path.os.path.join(root, file))
                path = path.replace('\\', '/')  # replace \ with /
                lucene_java_file_paths.append(path)  # add current path to our list
    return lucene_java_file_paths
