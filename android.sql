-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Lis 24, 2024 at 02:01 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `android`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `post`
--

CREATE TABLE `post` (
  `id` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(2000) NOT NULL,
  `name` varchar(100) NOT NULL,
  `status` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`id`, `created_at`, `description`, `name`, `status`, `updated_at`, `room_id`, `user_id`) VALUES
(1, '2024-11-24 13:59:48.000000', 'I really do think so:(', 'Yesterdays comic con was dumb !', 'ACTIVE', '2024-11-24 13:59:48.000000', 1, 2),
(2, '2024-11-24 13:59:52.000000', 'I really do think so:(', '12Yesterdays comic con was dumb !', 'ACTIVE', '2024-11-24 13:59:52.000000', 1, 2),
(3, '2024-11-24 13:59:53.000000', 'I really do think so:(', '13Yesterdays comic con was dumb !', 'ACTIVE', '2024-11-24 13:59:53.000000', 1, 2),
(4, '2024-11-24 13:59:59.000000', 'I really do think so:(', '13Yesterdays comic con was fun!', 'ACTIVE', '2024-11-24 13:59:59.000000', 1, 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `post_like`
--

CREATE TABLE `post_like` (
  `id` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `report`
--

CREATE TABLE `report` (
  `id` int(11) NOT NULL,
  `reason` varchar(255) NOT NULL,
  `status` enum('DISMISSED','PENDING','RESOLVED') NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `room`
--

CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`id`, `created_at`, `name`, `updated_at`) VALUES
(1, '2024-11-07 20:58:02.000000', 'askadmin', '2024-11-07 20:58:02.000000'),
(2, '2024-11-07 20:58:02.000000', 'gaming', '2024-11-07 20:58:02.000000'),
(3, '2024-11-07 20:58:02.000000', 'cosplay', '2024-11-07 20:58:02.000000'),
(4, '2024-11-07 20:58:02.000000', 'books', '2024-11-07 20:58:02.000000'),
(5, '2024-11-07 20:58:02.000000', 'gardening', '2024-11-07 20:58:02.000000'),
(6, '2024-11-07 20:58:02.000000', 'halloween', '2024-11-07 20:58:02.000000'),
(7, '2024-11-07 20:58:02.000000', 'beauty', '2024-11-07 20:58:02.000000'),
(8, '2024-11-07 20:58:02.000000', 'music', '2024-11-07 20:58:02.000000'),
(9, '2024-11-07 20:58:02.000000', 'science', '2024-11-07 20:58:02.000000'),
(10, '2024-11-07 20:58:02.000000', 'sports', '2024-11-07 20:58:02.000000'),
(11, '2024-11-07 20:58:02.000000', 'travel', '2024-11-07 20:58:02.000000'),
(12, '2024-11-07 20:58:02.000000', 'wellness', '2024-11-07 20:58:02.000000');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `created_at`, `email`, `password`, `role`, `status`, `updated_at`, `username`) VALUES
(1, '2024-11-24 13:26:22.000000', 'admin@gmail.com', '$2a$10$GENTVAN9Vu3WOjhmS6aHjeWMhPUX2bDaVkv7LDCVWv6.a.tEz1pki', 'ADMIN', 'alive', '2024-11-24 13:26:22.000000', 'admin'),
(2, '2024-11-24 13:27:32.000000', 'user@gmail.com', '$2a$10$GW1ufppF/UiUfXOQQRXnd.8nfcwibw.i1vMXwUKMmq6gz6KjikMEi', 'USER', 'alive', '2024-11-24 13:27:32.000000', 'user');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2wco6i4uyviie04sp98450ogx` (`room_id`),
  ADD KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`);

--
-- Indeksy dla tabeli `post_like`
--
ALTER TABLE `post_like`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKj7iy0k7n3d0vkh8o7ibjna884` (`post_id`),
  ADD KEY `FKhuh7nn7libqf645su27ytx21m` (`user_id`);

--
-- Indeksy dla tabeli `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnuqod1y014fp5bmqjeoffcgqy` (`post_id`),
  ADD KEY `FKj62onw73yx1qnmd57tcaa9q3a` (`user_id`);

--
-- Indeksy dla tabeli `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4l8mm4fqoos6fcbx76rvqxer` (`name`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `post`
--
ALTER TABLE `post`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `post_like`
--
ALTER TABLE `post_like`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FK2wco6i4uyviie04sp98450ogx` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  ADD CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `post_like`
--
ALTER TABLE `post_like`
  ADD CONSTRAINT `FKhuh7nn7libqf645su27ytx21m` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKj7iy0k7n3d0vkh8o7ibjna884` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

--
-- Constraints for table `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `FKj62onw73yx1qnmd57tcaa9q3a` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKnuqod1y014fp5bmqjeoffcgqy` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
