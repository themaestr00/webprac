package ru.ispras.wtpractice.videorent.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ispras.wtpractice.videorent.dao.*;
import ru.ispras.wtpractice.videorent.entity.Client;
import ru.ispras.wtpractice.videorent.entity.Exemplar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientDAO clientDAO;
    private final ExemplarDAO exemplarDAO;
    private final IssuedExemplarDAO issuedExemplarDAO;
    private final TransactionDAO transactionDAO;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) String by,
                       Model model) {

        var clients = (search == null || search.isBlank())
                ? clientDAO.findAll()
                : switch (by != null ? by : "") {
                    case "phone"   -> clientDAO.findByPhoneNumberContaining(search);
                    case "address" -> clientDAO.findByAddressContainingIgnoreCase(search);
                    default        -> clientDAO.findByFullNameContainingIgnoreCase(search);
                };

        Map<Long, Long> issuedCountMap = clients.stream()
                .collect(Collectors.toMap(
                        Client::getId,
                        c -> issuedExemplarDAO.countByIdClientId(c.getId())
                ));

        model.addAttribute("clients", clients);
        model.addAttribute("issuedCountMap", issuedCountMap);
        model.addAttribute("search", search);
        model.addAttribute("by", by);
        return "clients/list";
    }

    @GetMapping("/{id}")
    public String card(@PathVariable Long id, Model model) {
        Client client = clientDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));

        List<Long> exemplarIds = issuedExemplarDAO.findByIdClientId(id)
                .stream()
                .map(ie -> ie.getId().getExemplarId())
                .toList();
        List<Exemplar> issuedExemplars = exemplarDAO.findAllById(exemplarIds);

        model.addAttribute("client", client);
        model.addAttribute("issuedExemplars", issuedExemplars);
        model.addAttribute("transactions", transactionDAO.findByClientIdOrderByTimeDesc(id));
        return "clients/card";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Client client = clientDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));
        model.addAttribute("client", client);
        return "clients/form";
    }

    @PostMapping
    public String create(@ModelAttribute Client client,
                         @RequestParam("image") MultipartFile image) throws IOException {
        client.setImagePath(saveImage(image, null));
        clientDAO.save(client);
        return "redirect:/clients/" + client.getId();
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String fullName,
                         @RequestParam String phoneNumber,
                         @RequestParam String address,
                         @RequestParam("image") MultipartFile image) throws IOException {
        Client client = clientDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));

        client.setFullName(fullName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);

        if (!image.isEmpty()) {
            client.setImagePath(saveImage(image, null));
        }

        clientDAO.save(client);
        return "redirect:/clients/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        clientDAO.deleteById(id);
        return "redirect:/clients";
    }

    private String saveImage(MultipartFile image, String fallback) throws IOException {
        if (image == null || image.isEmpty()) {
            return fallback;
        }
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        String filename = Paths.get(image.getOriginalFilename()).getFileName().toString();
        Files.copy(image.getInputStream(), dir.resolve(filename),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + filename;
    }
}
