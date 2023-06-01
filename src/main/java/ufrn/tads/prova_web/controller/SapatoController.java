package ufrn.tads.prova_web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.tads.prova_web.model.Sapato;
import ufrn.tads.prova_web.service.FileStorageService;
import ufrn.tads.prova_web.service.SapatoService;
import ufrn.tads.prova_web.service.UsuarioService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SapatoController {
    SapatoService service;

    @Autowired
    FileStorageService fileStorageService;
    public SapatoController(SapatoService service, FileStorageService fileStorageService){
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String getIndex(Model model, HttpServletResponse response, HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", ""+authentication.getName());

        List<Sapato> carrinho = (List<Sapato>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        model.addAttribute("carrinho", carrinho);

        List<Sapato> sapatos = service.findByDeletedIsNull();
        model.addAttribute("sapatos",sapatos);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String dataHora = dateFormat.format(new Date());
        Cookie c = new Cookie("visita", dataHora);
        c.setMaxAge(86400);
        response.addCookie(c);

        return "index";
    }
    @GetMapping("/admin")
    public String getPagAdmin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", ""+authentication.getName());
        List<Sapato> sapatos = service.findByDeletedIsNull();
        model.addAttribute("sapatos", sapatos);

        return "admin/admin";
    }

    @GetMapping("/cadastrar")
    public String getCadastro(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", ""+authentication.getName());
        Sapato s = new Sapato();
        model.addAttribute("sapato", s);
        return "admin/cadastrar";
    }
    @PostMapping("/salvar")
    public String salvarSapato(@ModelAttribute @Valid Sapato s, Errors errors,
                               @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if(errors.hasErrors() || file.isEmpty()){
            System.out.println(Arrays.toString(errors.getAllErrors().toArray()));
            if(s.getId() == null){
                redirectAttributes.addFlashAttribute("msg", "*Verifique se os campos estão preenchidos corretamente");
                return "admin/cadastrar";
            }else{
                redirectAttributes.addFlashAttribute("msg", "*Verifique se os campos estão preenchidos corretamente");
                return "redirect:/editar/"+s.getId();
            }
        }else{
            s.setImageUri(this.fileStorageService.save(file));
            this.service.save(s);
            redirectAttributes.addFlashAttribute("msgSucesso", "Salvo com sucesso");
            return "redirect:/admin";
        }
    }
    @GetMapping("/editar/{id}")
    public String editarSapato(@PathVariable(name = "id") String id, Model model, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", ""+authentication.getName());
        Sapato s = service.findById(id);
        model.addAttribute("sapato", s);

        return "admin/editar";
    }

    @GetMapping("/deletar/{id}")
    public String deletarSapato(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes){
        Sapato s = service.findById(id);
        s.setDeleted(new Date(System.currentTimeMillis()));
        this.service.save(s);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/";
    }
    @GetMapping("/adicionarCarrinho/{id}")
    public String addCart(@PathVariable(name = "id") String id, HttpSession session){
        Sapato s = service.findById(id);
        List<Sapato> carrinho = (List<Sapato>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        carrinho.add(s);
        session.setAttribute("carrinho", carrinho);

        return "redirect:/index";
    }
    @GetMapping("/verCarrinho")
    public String getCard(HttpSession session, Model model, RedirectAttributes redirectAttributes){
        List<Sapato> carrinho = (List<Sapato>) session.getAttribute("carrinho");
        if(session.getAttribute("carrinho") == null){
            redirectAttributes.addAttribute("msg", "Não existem itens no carrinho");
            return "redirect:/";
        }
        model.addAttribute("carrinho", carrinho);
        return "carrinho";
    }
    @GetMapping("/finalizarCompra")
    public String finalizar(HttpSession session){
        session.invalidate();
        return "redirect:/index";
    }
    @GetMapping("/remover/{id}")
    public String removerItemCarrinho(@PathVariable(name = "id") String id, HttpSession session, RedirectAttributes redirectAttributes){
        Sapato s = service.findById(id);
        List<Sapato> carrinho = (List<Sapato>) session.getAttribute("carrinho");
        for (int i = 0; i < carrinho.size(); i++) {
            if(carrinho.get(i).equals(s)){
                carrinho.remove(s);
            }
        }
        session.setAttribute("carrinho", carrinho);

        return "redirect:/verCarrinho";
    }
}
