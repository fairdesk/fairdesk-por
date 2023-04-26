package com.fairdesk.util;

import java.util.ArrayList;
import java.util.List;

import static com.fairdesk.util.HashUtils.sha256;

public class MerkleTreeUtils {
    public static List<String> merge(List<String> userHashList) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < userHashList.size(); i += 2) {
            String left = userHashList.get(i);
            String right = i == (userHashList.size() - 1) ? "" : userHashList.get(i + 1);

            String merkleTreeRootHash = sha256(left + "," + right).substring(0, 16);
            result.add(merkleTreeRootHash);
        }

        while (result.size() > 1) {
            result = merge(result);
        }

        return result;
    }
}
