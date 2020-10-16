package com.lino.vendas.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lino.vendas.domain.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class JwtService {

	@Value("${security.jwt.expiracao}")
	private String expiracao;
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;
	
	public String gerarToken(Usuario usuario) {
		
		long expString = Long.valueOf(expiracao);//pegou a string que contem o tempo de expiração e converteu para o tipo long
		LocalDateTime dataHorraExpiracao = LocalDateTime.now().plusMinutes(expString);
		//esta pegando a data e hora atual e somando com a variavel (expString) para definir a hora que vai encerrar a sessão 
		Date data = Date.from(dataHorraExpiracao.atZone(ZoneId.systemDefault()).toInstant());//esta pegando uma variavel do tipo LocalDateTime e convertendo em Date
		
		HashMap<String, Object> claims = new HashMap<String, Object>();
	
		
		return Jwts
					.builder()
					.setSubject(usuario.getLogin())
					.setExpiration(data)
					.signWith( SignatureAlgorithm.HS512,chaveAssinatura)
					.compact();
	}
	
	private Claims obterClaims(String token) throws ExpiredJwtException{
		
		
		return Jwts
				.parser()
				.setSigningKey(chaveAssinatura)
				.parseClaimsJws(token)
				.getBody();
		
	}
	
	public boolean tokenValido( String token) {
		try {
			Claims claims = obterClaims(token);
			Date dataExpiracao = claims.getExpiration();
			LocalDateTime data = dataExpiracao.toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDateTime();//converteu do tipo date para o tipo LocalDateTime
	
			return !LocalDateTime.now().isAfter(data);
			
		}catch(Exception e) {
			return false;
		}
	}
	
	
	public String obterLoginUsuario(String token) throws ExpiredJwtException {
		
		return (String) obterClaims(token).getSubject();
		
		
	}
	
//	public static void main(String[] args) {
//		ConfigurableApplicationContext contexto = SpringApplication.run(VendasApplication.class);
//		JwtService service = contexto.getBean(JwtService.class);
//		Usuario usuario = Usuario.builder().login("fulano").build();
//		String token = service.gerarToken(usuario);
//		System.out.println(token);
//		
//		boolean isTokenValido = service.tokenValido(token);
//		System.out.println("O token esta valido: "+isTokenValido);
//		System.out.println(service.obterLoginUsuario(token));
//		
//	}
	
}
