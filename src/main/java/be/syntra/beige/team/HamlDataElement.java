package be.syntra.beige.team;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>HamlDataElement</h1>
 * HamlDataElement object contains structured data of 1 line in a .haml file.
 * It gets populated by HamlConverter and added to arraylist hamlDataElements in HamlData object.
 * <p>
 * To be added:
 * list of all variables
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 *
 *
 *
 *
 */

public class HamlDataElement {
    /**
     *
     * Variables
     */

    // Required variables
    //
    private int lineNumber;
    private int depth;

    private boolean isTag;
    private String tagName;

    private boolean hasText;
    private String textContent;

    private boolean isComment;
    private String commentType;
    private String commentContent;

    private boolean hasEscaping;
    private String escapedContent;

    private boolean hasWhiteSpaceRemoval;
    private String whiteSpaceRemovalType;

    // Optional variables
    //
    private String id;
    private String className;
    private HashMap<String,String> attributes;

    private HamlDataElement parent;
    private ArrayList<HamlDataElement> children;

    /**
     *
     * Constructors
     */
    public HamlDataElement(int lineNumber,
                           int depth,
                           boolean isTag,
                           String tagName,
                           boolean hasText,
                           String textContent,
                           boolean isComment,
                           String commentType,
                           String commentContent,
                           boolean hasEscaping,
                           String escapedContent,
                           boolean hasWhiteSpaceRemoval,
                           String whiteSpaceRemovalType)
    {
        this.lineNumber = lineNumber;
        this.depth = depth;
        this.isTag = isTag;
        this.tagName = tagName;
        this.hasText = hasText;
        this.textContent = textContent;
        this.isComment = isComment;
        this.commentType = commentType;
        this.commentContent = commentContent;
        this.hasEscaping = hasEscaping;
        this.escapedContent = escapedContent;
        this.hasWhiteSpaceRemoval = hasWhiteSpaceRemoval;
        this.whiteSpaceRemovalType = whiteSpaceRemovalType;
        this.children = new ArrayList<>();
    }

    public HamlDataElement(int lineNumber,
                           int depth,
                           boolean isTag,
                           String tagName,
                           boolean hasText,
                           String textContent,
                           boolean isComment,
                           String commentType,
                           String commentContent,
                           boolean hasEscaping,
                           String escapedContent,
                           boolean hasWhiteSpaceRemoval,
                           String whiteSpaceRemovalType,
                           String id)
    {
        this.lineNumber = lineNumber;
        this.depth = depth;
        this.isTag = isTag;
        this.tagName = tagName;
        this.hasText = hasText;
        this.textContent = textContent;
        this.isComment = isComment;
        this.commentType = commentType;
        this.commentContent = commentContent;
        this.hasEscaping = hasEscaping;
        this.escapedContent = escapedContent;
        this.hasWhiteSpaceRemoval = hasWhiteSpaceRemoval;
        this.whiteSpaceRemovalType = whiteSpaceRemovalType;
        this.id = id;
        this.children = new ArrayList<>();
    }

    public HamlDataElement(int lineNumber,
                           int depth,
                           boolean isTag,
                           String tagName,
                           boolean hasText,
                           String textContent,
                           boolean isComment,
                           String commentType,
                           String commentContent,
                           boolean hasEscaping,
                           String escapedContent,
                           boolean hasWhiteSpaceRemoval,
                           String whiteSpaceRemovalType,
                           String id,
                           String className)
    {
        this.lineNumber = lineNumber;
        this.depth = depth;
        this.isTag = isTag;
        this.tagName = tagName;
        this.hasText = hasText;
        this.textContent = textContent;
        this.isComment = isComment;
        this.commentType = commentType;
        this.commentContent = commentContent;
        this.hasEscaping = hasEscaping;
        this.escapedContent = escapedContent;
        this.hasWhiteSpaceRemoval = hasWhiteSpaceRemoval;
        this.whiteSpaceRemovalType = whiteSpaceRemovalType;
        this.id = id;
        this.className = className;
        this.children = new ArrayList<>();
    }

    public HamlDataElement(int lineNumber,
                           int depth,
                           boolean isTag,
                           String tagName,
                           boolean hasText,
                           String textContent,
                           boolean isComment,
                           String commentType,
                           String commentContent,
                           boolean hasEscaping,
                           String escapedContent,
                           boolean hasWhiteSpaceRemoval,
                           String whiteSpaceRemovalType,
                           String id,
                           String className,
                           HashMap<String, String> attributes)
    {
        this.lineNumber = lineNumber;
        this.depth = depth;
        this.isTag = isTag;
        this.tagName = tagName;
        this.hasText = hasText;
        this.textContent = textContent;
        this.isComment = isComment;
        this.commentType = commentType;
        this.commentContent = commentContent;
        this.hasEscaping = hasEscaping;
        this.escapedContent = escapedContent;
        this.hasWhiteSpaceRemoval = hasWhiteSpaceRemoval;
        this.whiteSpaceRemovalType = whiteSpaceRemovalType;
        this.id = id;
        this.className = className;
        this.attributes = attributes;
        this.children = new ArrayList<>();
    }

    /**
     * Functions
     */
    // This is kind of a setter, but also not really; adds a child to the above list
    public void addChild(HamlDataElement child){
        if (this.children == null) {
            this.children = new ArrayList<HamlDataElement>();
        }

        this.children.add(child);
    }

    // Overrides
    //
    @Override
    public String toString() {
        return "HamlDataElement => " +
                "lineNumber=" + lineNumber +
                ", depth=" + depth +
                ", isTag=" + isTag +
                ", tagName='" + tagName + '\'' +
                ", hasText=" + hasText +
                ", textContent='" + textContent + '\'' +
                ", isComment=" + isComment +
                ", commentType='" + commentType + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", hasEscaping=" + hasEscaping +
                ", escapedContent='" + escapedContent + '\'' +
                ", hasWhiteSpaceRemoval=" + hasWhiteSpaceRemoval +
                ", whiteSpaceRemovalType='" + whiteSpaceRemovalType + '\'' +
                ", id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", attributes=" + attributes;
    }


    /**
     * Getters
     */
    public int getLineNumber() {
        return lineNumber;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isTag() {
        return isTag;
    }

    public String getTagName() {
        return tagName;
    }

    public boolean hasText() {
        return hasText;
    }

    public String getTextContent() {
        return textContent;
    }

    public boolean isComment() {
        return isComment;
    }

    public String getCommentType() {
        return commentType;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public boolean isHasEscaping() {
        return hasEscaping;
    }

    public String getEscapedContent() {
        return escapedContent;
    }

    public boolean hasWhiteSpaceRemoval() {
        return hasWhiteSpaceRemoval;
    }

    public String getWhiteSpaceRemovalType() {
        return whiteSpaceRemovalType;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public HamlDataElement getParent() {
        return parent;
    }

    public ArrayList<HamlDataElement> getChildren() {
        return children;
    }

    public HamlDataElement getChild(int id) {
        if (children.size() < id) {
            return null;
        }

        return children.get(id);
    }


    /**
     * Setters
     */
    // Todo
    // The setters for the required variables should probably be deleted,
    // since they MUST be in the object via constructors
    // -- BELOW MARKED AS OBSOLETE --
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setTag(boolean tag) {
        isTag = tag;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setText(boolean text) {
        hasText = text;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public void setHasEscaping(boolean hasEscaping) {
        this.hasEscaping = hasEscaping;
    }

    public void setEscapedContent(String escapedContent) {
        this.escapedContent = escapedContent;
    }

    public void setHasWhiteSpaceRemoval(boolean hasWhiteSpaceRemoval) {
        this.hasWhiteSpaceRemoval = hasWhiteSpaceRemoval;
    }

    public void setWhiteSpaceRemovalType(String whiteSpaceRemovalType) {
        this.whiteSpaceRemovalType = whiteSpaceRemovalType;
    }
    // -- ABOVE MARKED AS OBSOLETE --

    // These are probably the only setter methods that are really necessary
    // (the ones for the optional parameters of HamlDataElement)
    //
    public void setId(String id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setParent(HamlDataElement parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<HamlDataElement> children) {
        this.children = children;
    }
}
