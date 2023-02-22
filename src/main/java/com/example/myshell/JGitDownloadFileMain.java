package com.example.myshell;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.File;
import java.io.FileOutputStream;

public class JGitDownloadFileMain {
    public static void main(String[] args) {
        try {

            Git git = Git.cloneRepository().setURI("https://github.com/sachitras/shell-demo")
                    .setDirectory(new File("D:\\Sachitra\\ExternalProjects\\FileDownload\\GIT"))
                    .call();
            Repository repository = git.getRepository();

            // find the HEAD
            ObjectId lastCommitId = repository.resolve(Constants.HEAD);

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(lastCommitId);
                // and using commit's tree find the path
                RevTree tree = commit.getTree();
                System.out.println("Having tree: " + tree);

                // now try to find a specific file
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create("pom.xml"));
                    if (!treeWalk.next()) {
                        throw new IllegalStateException("Did not find expected file 'pom.xml'");
                    }

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);

                    // and then one can the loader to read the file
                    loader.copyTo(System.out);
                    FileOutputStream outFile = new FileOutputStream("D:\\\\Sachitra\\\\ExternalProjects\\\\FileDownload\\\\out4.txt");
                    loader.copyTo(outFile);
                }

                revWalk.dispose();
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
