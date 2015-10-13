package it.feio.android.omninotes.utils;

import android.content.Context;
import android.util.Pair;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.models.Tag;
import it.feio.android.pixlui.links.RegexPatternsConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;


public class TagsHelper {


    public static List<Tag> getAllTags(Context context) {
        return DbHelper.getInstance(context).getTags();
    }


    public static HashMap<String, Integer> retrieveTags(Note note) {
        HashMap<String, Integer> tagsMap = new HashMap<String, Integer>();
        Matcher matcher = RegexPatternsConstants.HASH_TAG.matcher(note.getTitle() + " " + note.getContent());
        while (matcher.find()) {
            String tagText = matcher.group().trim();
            int count = tagsMap.get(tagText) == null ? 0 : tagsMap.get(tagText);
            tagsMap.put(tagText, ++count);
        }
        return tagsMap;
    }


    public static Pair<String, List<Tag>> addTagToNote(List<Tag> tags, Integer[] selectedTags, Note note) {
        StringBuilder sbTags = new StringBuilder();
        List<Tag> tagsToRemove = new ArrayList<Tag>();
        HashMap<String, Integer> tagsMap = retrieveTags(note);

        List<Integer> selectedTagsList = Arrays.asList(selectedTags);
        for (int i = 0; i < tags.size(); i++) {
            if (mapContainsTag(tagsMap, tags.get(i))) {
                if (!selectedTagsList.contains(i)) {
                    tagsToRemove.add(tags.get(i));
                }
            } else {
                if (selectedTagsList.contains(i)) {
                    if (sbTags.length() > 0) {
                        sbTags.append(" ");
                    }
                    sbTags.append(tags.get(i));
                }
            }
        }
        return new Pair(sbTags.toString(), tagsToRemove);
    }


    private static boolean mapContainsTag(HashMap<String, Integer> tagsMap, Tag tag) {
        for (String tagsMapItem : tagsMap.keySet()) {
            if (tagsMapItem.equals(tag.getText())) {
                return true;
            }
        }
        return false;
    }


    public static Pair<String, String> removeTag(String noteTitle, String noteContent, List<Tag> tagsToRemove) {
        String title = noteTitle, content = noteContent;
        for (Tag tagToRemove : tagsToRemove) {
            title = title.replaceAll(tagToRemove.getText(), "");
            content = content.replaceAll(tagToRemove.getText(), "");
        }
        return new Pair<String, String>(title, content);
    }


    public static String[] getTagsArray(List<Tag> tags) {
        String[] tagsArray = new String[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            tagsArray[i] = tags.get(i).getText() + " (" + tags.get(i).getCount() + ")";
        }
        return tagsArray;
    }


    public static Integer[] getPreselectedTagsArray(Note note, List<Tag> tags) {
        List<Note> notes = new ArrayList<Note>();
        notes.add(note);
        return getPreselectedTagsArray(notes, tags);
    }


    public static Integer[] getPreselectedTagsArray(List<Note> notes, List<Tag> tags) {
        final Integer[] preSelectedTags;
        if (notes.size() == 1) {
            List<Integer> t = new ArrayList<Integer>();
            for (String noteTag : TagsHelper.retrieveTags(notes.get(0)).keySet()) {
                for (Tag tag : tags) {
                    if (tag.getText().equals(noteTag)) {
                        t.add(tags.indexOf(tag));
                        break;
                    }
                }
            }
            preSelectedTags = t.toArray(new Integer[t.size()]);
        } else {
            preSelectedTags = new Integer[]{};
        }
        return preSelectedTags;
    }
}
