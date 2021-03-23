package com.junyi.permission.model;

import lombok.Data;

import java.util.List;

@Data
public class Node {
    private String id;
    private String label;
    private boolean checked;
    private List<Node> children;

    public Node(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public Node(String id, String label, boolean checked) {
        this(id, label);
        this.checked = checked;
    }
}
