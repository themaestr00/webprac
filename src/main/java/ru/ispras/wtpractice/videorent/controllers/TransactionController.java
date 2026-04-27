package ru.ispras.wtpractice.videorent.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ispras.wtpractice.videorent.dao.*;
import ru.ispras.wtpractice.videorent.dbtypes.TransactionType;
import ru.ispras.wtpractice.videorent.entity.*;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionDAO transactionDAO;
    private final ClientDAO clientDAO;
    private final ExemplarDAO exemplarDAO;

    @GetMapping("/issue")
    public String issueForm(@RequestParam(required = false) Long clientId,
                            @RequestParam(required = false) Long exemplarId,
                            Model model) {
        model.addAttribute("clients", clientDAO.findAll());
        model.addAttribute("availableExemplars", exemplarDAO.findByAvailableIsTrue());
        model.addAttribute("selectedClientId", clientId);
        model.addAttribute("selectedExemplarId", exemplarId);
        return "issue";
    }

    @PostMapping("/issue")
    public String issue(@RequestParam Long clientId,
                        @RequestParam Long exemplarId) {
        Client client   = clientDAO.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));
        Exemplar exemplar = exemplarDAO.findById(exemplarId)
                .orElseThrow(() -> new RuntimeException("Exemplar not found: " + exemplarId));

        transactionDAO.save(new Transaction(null, exemplar, client, null, TransactionType.issue));

        return "redirect:/clients/" + clientId;
    }

    @PostMapping("/return")
    public String returning(@RequestParam Long clientId,
                            @RequestParam Long exemplarId) {
        Client client   = clientDAO.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));
        Exemplar exemplar = exemplarDAO.findById(exemplarId)
                .orElseThrow(() -> new RuntimeException("Exemplar not found: " + exemplarId));

        transactionDAO.save(new Transaction(null, exemplar, client, null, TransactionType.returning));

        return "redirect:/clients/" + clientId;
    }
}
