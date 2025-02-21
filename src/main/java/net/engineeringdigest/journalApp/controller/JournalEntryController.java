package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.schema.JournalSchema;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody JournalSchema journalSchema) {
        try {
            journalSchema.setDate(LocalDateTime.now());
            boolean res = journalEntryService.saveEntry(journalSchema);
            if (res)
                return new ResponseEntity<>(journalSchema, HttpStatus.CREATED);
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEntry(@PathVariable ObjectId id) {
        Optional<JournalSchema> journalEntry = journalEntryService.getJournalEntry(id);

        if (!journalEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
        }

        if (journalEntryService.isUserAssociated(id)) {
            return ResponseEntity.ok(journalEntry.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Entry is not associated with the requested User");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<JournalSchema>> getAllEntries() {
        return new ResponseEntity<>(journalEntryService.getJournalEntries(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody JournalSchema newEntry) {
        Optional<JournalSchema> oldEntry = journalEntryService.getJournalEntry(id);

        if (!oldEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
        }

        if (journalEntryService.isUserAssociated(id)) {
            JournalSchema journalEntry = oldEntry.get();
            journalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : journalEntry.getTitle());
            journalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : journalEntry.getContent());

            journalEntryService.updateJournalEntry(journalEntry);

            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Entry is not associated with the requested User");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        Optional<JournalSchema> journalEntry = journalEntryService.getJournalEntry(id);

        if (!journalEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
        }

        if (journalEntryService.isUserAssociated(id)) {
            journalEntryService.deleteJournalEntry(id);
            return ResponseEntity.ok("Journal entry deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Entry is not associated with the requested User");
        }

    }
}
