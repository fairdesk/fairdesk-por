package com.fairdesk;

import com.alibaba.fastjson.JSON;
import com.fairdesk.merkletree.MerkleNode;
import com.fairdesk.merkletree.MerkleTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fairdesk.util.HashUtils.getUserHash;
import static com.fairdesk.util.MerkleTreeUtils.merge;

public class MerkleTreeValidator {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("config not found, please use java -jar target/faidesk-merkletree-validator.jar.jar path/to/binfile hash");
            return;
        }
        String binFilePath = args[0];
        String hash = args[1];

        File file = new File(binFilePath);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (FileNotFoundException e) {
            System.out.println("binfile not found, please check your config");
            return;
        }

        MerkleTree merkleTree = MerkleTree.parseFrom(bytes);

        String originMerkleRootHash = merkleTree.getRootHash();

        List<MerkleNode> merkleNodes = merkleTree.getNodesList();
        List<String> currencyList = merkleTree.getCurrencyList();

        Map<String, String> userBalance = new HashMap<>();
        Map<String, BigDecimal> totalUserBalance = new HashMap<>();
        List<String> userHashList = merkleNodes.stream().map(merkleNode -> {
            StringBuilder balanceSb = new StringBuilder();
            currencyList.forEach(currency -> {
                String balance = merkleNode.getBalanceMap().get(currency);
                balanceSb.append(currency).append(balance);
                totalUserBalance.compute(currency, (k, v) -> {
                   if (v == null) {
                       return new BigDecimal(balance);
                   }

                   return v.add(new BigDecimal(balance));
                });
            });
            String userHash = getUserHash(merkleNode.getUid(), merkleNode.getNonce(), balanceSb.toString());

            if (hash.equalsIgnoreCase(userHash)) {
                if (!userHash.equalsIgnoreCase(merkleNode.getHash())) {
                    System.out.printf("merkelTree file validate failed, actual hash is: %s, hash in file is: %s", userHash, merkleNode.getHash());
                } else {
                    userBalance.putAll(merkleNode.getBalanceMap());
                }
            }
            return merkleNode.getHash();
        }).collect(Collectors.toList());
        String newMerkleRootHash = merge(userHashList).get(0);

        if (originMerkleRootHash.equalsIgnoreCase(newMerkleRootHash) && !userBalance.isEmpty()) {
            System.out.printf("validate success!\n your balance is: %s\n total user balance: %s\n", JSON.toJSON(userBalance), JSON.toJSON(totalUserBalance));
        } else if (userBalance.isEmpty()){
            System.out.printf("validate failed, hash %s not found\n", hash);
        } else {
            System.out.println("validate failed");
        }
    }
}
