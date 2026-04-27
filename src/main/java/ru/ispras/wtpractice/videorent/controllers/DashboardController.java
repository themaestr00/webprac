package ru.ispras.wtpractice.videorent.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ispras.wtpractice.videorent.dao.*;
import ru.ispras.wtpractice.videorent.dbtypes.TransactionType;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ClientDAO clientDAO;
    private final MovieDAO movieDAO;
    private final ExemplarDAO exemplarDAO;
    private final TransactionDAO transactionDAO;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("clientCount", clientDAO.count());
        model.addAttribute("movieCount", movieDAO.count());
        model.addAttribute("exemplarCount", exemplarDAO.count());
        model.addAttribute("availableCount", exemplarDAO.findByAvailableIsTrue().size());

        model.addAttribute("recentTransactions",
                transactionDAO.findByTypeOrderByTimeDesc(TransactionType.issue)
                        .stream().limit(10).toList()
        );

        return "dashboard";
    }
}
