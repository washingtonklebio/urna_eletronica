

USE urna;

CREATE TABLE IF NOT EXISTS usuarios(
    id INT AUTO_INCREMENT, 
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO `usuarios`( `login`, `senha`) VALUES ("admin", "123");

CREATE TABLE `eleitores` (
  `id` int(11) AUTO_INCREMENT NOT NULL,
  `nome` varchar(60) NOT NULL,
  `dtnasc` varchar(10) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `eleitores` (`id`, `nome`, `dtnasc`, `cpf`) VALUES
(1, 'Victor Alves Fialho', '12/01/1997', '123.456.789-10');

CREATE TABLE `candidatos` (
  `numero` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `chapa` varchar(40) NOT NULL,
  `cargo` varchar(15) NOT NULL,
  `caminho` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `candidatos`
  ADD UNIQUE KEY `numero` (`numero`);
COMMIT;

INSERT INTO `candidatos` (`numero`, `nome`, `chapa`, `cargo`, `caminho`) VALUES
(12, 'Ciro Gomes', 'PDT', 'Presidente', 'ciro.jpg'),
(17, 'Jair Messias Bolsonaro', 'PSL', 'Presidente', 'bolsonaro.jpg'),
(30, 'Joao Amoedo', 'Novo', 'Presidente', 'amoedo.jpg'),
(51, 'Cabo Daciolo', 'Patri', 'Presidente', 'daciolo.jpg');

CREATE TABLE IF NOT EXISTS votos(
    id_voto INT AUTO_INCREMENT,
    numero INT,
    validos INT DEFAULT 0,
    brancos INT DEFAULT 0,
    nao_validos INT DEFAULT 0,
    PRIMARY KEY(id_voto)
);