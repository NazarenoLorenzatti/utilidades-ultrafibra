package com.ultrafibra.utilidades.security;

import com.ultrafibra.utilidades.DAO.usuariosDAO.iUsuarioDao;
import com.ultrafibra.utilidades.domain.usuarios.Rol;
import com.ultrafibra.utilidades.domain.usuarios.Usuario;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private iUsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Rol rol : usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
        }
        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(), usuario.getPassword(), authorities);
    }

}
