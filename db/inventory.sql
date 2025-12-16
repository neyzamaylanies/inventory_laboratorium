-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 16 Des 2025 pada 09.32
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventory`
--

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `active_borrowings`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `active_borrowings` (
`transaction_id` varchar(15)
,`borrow_date` datetime
,`equipment_name` varchar(150)
,`quantity` int(11)
,`nim` varchar(20)
,`student_name` varchar(100)
,`study_program` varchar(100)
,`handled_by_name` varchar(100)
,`handled_by_role` enum('ADMIN','PETUGAS')
,`note` text
);

-- --------------------------------------------------------

--
-- Struktur dari tabel `equipment_category`
--

CREATE TABLE `equipment_category` (
  `id` varchar(10) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `equipment_category`
--

INSERT INTO `equipment_category` (`id`, `category_name`, `description`, `created_at`, `updated_at`) VALUES
('CAT001', 'Alat Ukur', 'Alat untuk mengukur besaran fisika', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('CAT002', 'Alat Gelas', 'Peralatan berbahan gelas untuk laboratorium', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('CAT003', 'Mikroskop', 'Alat untuk melihat objek mikroskopis', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('CAT004', 'Elektronik', 'Peralatan elektronik laboratorium', '2025-12-16 07:25:27', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Struktur dari tabel `equipment_condition_log`
--

CREATE TABLE `equipment_condition_log` (
  `id` varchar(15) NOT NULL,
  `equipment_id` varchar(10) NOT NULL,
  `condition_before` enum('BAIK','RUSAK_RINGAN','RUSAK_BERAT','DALAM_PERBAIKAN') NOT NULL,
  `condition_after` enum('BAIK','RUSAK_RINGAN','RUSAK_BERAT','DALAM_PERBAIKAN') NOT NULL,
  `check_date` datetime NOT NULL DEFAULT current_timestamp(),
  `checked_by` varchar(10) NOT NULL,
  `note` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `equipment_condition_log`
--

INSERT INTO `equipment_condition_log` (`id`, `equipment_id`, `condition_before`, `condition_after`, `check_date`, `checked_by`, `note`, `created_at`) VALUES
('LOG20250116001', 'EQP003', 'BAIK', 'RUSAK_RINGAN', '2025-01-16 17:00:00', 'EMP001', 'Kabel probe aus, perlu ganti', '2025-12-16 07:25:27'),
('LOG20250117001', 'EQP001', 'BAIK', 'BAIK', '2025-01-17 15:30:00', 'EMP001', 'Kondisi tetap baik setelah praktikum', '2025-12-16 07:25:27'),
('LOG20250117002', 'EQP002', 'BAIK', 'BAIK', '2025-01-17 16:30:00', 'EMP002', 'Tidak ada kerusakan', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `equipment_stock_realtime`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `equipment_stock_realtime` (
`id` varchar(10)
,`equipment_name` varchar(150)
,`category_name` varchar(100)
,`total_quantity` int(11)
,`available_quantity` int(11)
,`quantity_in_use` bigint(12)
,`condition_status` enum('BAIK','RUSAK_RINGAN','RUSAK_BERAT','DALAM_PERBAIKAN')
,`location` varchar(100)
);

-- --------------------------------------------------------

--
-- Struktur dari tabel `inventory_transaction`
--

CREATE TABLE `inventory_transaction` (
  `id` varchar(15) NOT NULL,
  `equipment_id` varchar(10) NOT NULL,
  `transaction_type` enum('IN','OUT') NOT NULL,
  `quantity` int(11) NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT current_timestamp(),
  `note` text DEFAULT NULL,
  `handled_by` varchar(10) NOT NULL,
  `used_by` varchar(10) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `inventory_transaction`
--

INSERT INTO `inventory_transaction` (`id`, `equipment_id`, `transaction_type`, `quantity`, `transaction_date`, `note`, `handled_by`, `used_by`, `created_at`) VALUES
('TRX20230115001', 'EQP001', 'IN', 10, '2023-01-15 09:00:00', 'Pengadaan awal mikroskop', 'ADM001', NULL, '2025-12-16 07:25:27'),
('TRX20230220001', 'EQP002', 'IN', 50, '2023-02-20 10:30:00', 'Pengadaan beaker glass', 'ADM001', NULL, '2025-12-16 07:25:27'),
('TRX20250115001', 'EQP001', 'OUT', 2, '2025-01-15 08:00:00', 'Praktikum Biologi Sel', 'EMP001', 'STD001', '2025-12-16 07:25:27'),
('TRX20250115002', 'EQP002', 'OUT', 5, '2025-01-15 09:00:00', 'Praktikum Kimia Dasar', 'EMP002', 'STD002', '2025-12-16 07:25:27'),
('TRX20250116001', 'EQP003', 'OUT', 3, '2025-01-16 10:00:00', 'Praktikum Elektronika', 'EMP001', 'STD003', '2025-12-16 07:25:27'),
('TRX20250117001', 'EQP001', 'IN', 2, '2025-01-17 15:00:00', 'Pengembalian mikroskop', 'EMP001', 'STD001', '2025-12-16 07:25:27'),
('TRX20250117002', 'EQP002', 'IN', 5, '2025-01-17 16:00:00', 'Pengembalian beaker glass', 'EMP002', 'STD002', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Struktur dari tabel `laboratory_equipment`
--

CREATE TABLE `laboratory_equipment` (
  `id` varchar(10) NOT NULL,
  `equipment_name` varchar(150) NOT NULL,
  `category_id` varchar(10) NOT NULL,
  `total_quantity` int(11) NOT NULL DEFAULT 0,
  `available_quantity` int(11) NOT NULL DEFAULT 0,
  `condition_status` enum('BAIK','RUSAK_RINGAN','RUSAK_BERAT','DALAM_PERBAIKAN') DEFAULT 'BAIK',
  `location` varchar(100) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `purchase_price` decimal(15,2) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `laboratory_equipment`
--

INSERT INTO `laboratory_equipment` (`id`, `equipment_name`, `category_id`, `total_quantity`, `available_quantity`, `condition_status`, `location`, `purchase_date`, `purchase_price`, `created_at`, `updated_at`) VALUES
('EQP001', 'Mikroskop Binokuler', 'CAT003', 10, 8, 'BAIK', 'Lab Biologi Lt.2', '2023-01-15', 15000000.00, '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EQP002', 'Beaker Glass 500ml', 'CAT002', 50, 45, 'BAIK', 'Lab Kimia Lt.1', '2023-02-20', 150000.00, '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EQP003', 'Multimeter Digital', 'CAT004', 15, 12, 'BAIK', 'Lab Fisika Lt.3', '2023-03-10', 750000.00, '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EQP004', 'Pipet Volumetrik 25ml', 'CAT002', 30, 28, 'BAIK', 'Lab Kimia Lt.1', '2023-04-05', 85000.00, '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EQP005', 'pH Meter Digital', 'CAT001', 8, 7, 'BAIK', 'Lab Kimia Lt.1', '2023-05-12', 2500000.00, '2025-12-16 07:25:27', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Struktur dari tabel `student`
--

CREATE TABLE `student` (
  `id` varchar(10) NOT NULL,
  `nim` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `study_program` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `student`
--

INSERT INTO `student` (`id`, `nim`, `name`, `study_program`, `phone`, `created_at`, `updated_at`) VALUES
('STD001', '2021110001', 'Andi Pratama', 'Teknik Kimia', '081234567890', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('STD002', '2021110002', 'Dewi Lestari', 'Biologi', '081234567891', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('STD003', '2021110003', 'Reza Firmansyah', 'Fisika', '081234567892', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('STD004', '2020110004', 'Sinta Maharani', 'Kimia', '081234567893', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('STD005', '2021110005', 'Budi Setiawan', 'Teknik Mesin', '081234567894', '2025-12-16 07:25:27', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `transaction_history`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `transaction_history` (
`id` varchar(15)
,`transaction_date` datetime
,`transaction_type` enum('IN','OUT')
,`equipment_name` varchar(150)
,`category_name` varchar(100)
,`quantity` int(11)
,`handled_by_name` varchar(100)
,`handled_by_role` enum('ADMIN','PETUGAS')
,`student_name` varchar(100)
,`student_nim` varchar(20)
,`note` text
);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','PETUGAS') DEFAULT 'PETUGAS',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `created_at`, `updated_at`) VALUES
('ADM001', 'Dr. Budi Santoso', 'budi.santoso@lab.ac.id', '$2y$10$examplehash1', 'ADMIN', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('ADM002', 'Prof. Sinta Dewi', 'sinta.dewi@lab.ac.id', '$2y$10$examplehash2', 'ADMIN', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EMP001', 'Siti Nurhaliza', 'siti.nurhaliza@lab.ac.id', '$2y$10$examplehash3', 'PETUGAS', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EMP002', 'Ahmad Rifai', 'ahmad.rifai@lab.ac.id', '$2y$10$examplehash4', 'PETUGAS', '2025-12-16 07:25:27', '2025-12-16 07:25:27'),
('EMP003', 'Dewi Lestari', 'dewi.lestari@lab.ac.id', '$2y$10$examplehash5', 'PETUGAS', '2025-12-16 07:25:27', '2025-12-16 07:25:27');

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `user_list`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `user_list` (
`id` varchar(10)
,`name` varchar(100)
,`email` varchar(100)
,`role` enum('ADMIN','PETUGAS')
,`role_display` varchar(13)
,`created_at` timestamp
);

-- --------------------------------------------------------

--
-- Struktur untuk view `active_borrowings`
--
DROP TABLE IF EXISTS `active_borrowings`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `active_borrowings`  AS SELECT `t`.`id` AS `transaction_id`, `t`.`transaction_date` AS `borrow_date`, `e`.`equipment_name` AS `equipment_name`, `t`.`quantity` AS `quantity`, `s`.`nim` AS `nim`, `s`.`name` AS `student_name`, `s`.`study_program` AS `study_program`, `u`.`name` AS `handled_by_name`, `u`.`role` AS `handled_by_role`, `t`.`note` AS `note` FROM (((`inventory_transaction` `t` join `laboratory_equipment` `e` on(`t`.`equipment_id` = `e`.`id`)) join `student` `s` on(`t`.`used_by` = `s`.`id`)) join `users` `u` on(`t`.`handled_by` = `u`.`id`)) WHERE `t`.`transaction_type` = 'OUT' AND `t`.`used_by` is not null AND !exists(select 1 from `inventory_transaction` `t2` where `t2`.`equipment_id` = `t`.`equipment_id` AND `t2`.`used_by` = `t`.`used_by` AND `t2`.`transaction_type` = 'IN' AND `t2`.`transaction_date` > `t`.`transaction_date` limit 1) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `equipment_stock_realtime`
--
DROP TABLE IF EXISTS `equipment_stock_realtime`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `equipment_stock_realtime`  AS SELECT `e`.`id` AS `id`, `e`.`equipment_name` AS `equipment_name`, `c`.`category_name` AS `category_name`, `e`.`total_quantity` AS `total_quantity`, `e`.`available_quantity` AS `available_quantity`, `e`.`total_quantity`- `e`.`available_quantity` AS `quantity_in_use`, `e`.`condition_status` AS `condition_status`, `e`.`location` AS `location` FROM (`laboratory_equipment` `e` join `equipment_category` `c` on(`e`.`category_id` = `c`.`id`)) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `transaction_history`
--
DROP TABLE IF EXISTS `transaction_history`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `transaction_history`  AS SELECT `t`.`id` AS `id`, `t`.`transaction_date` AS `transaction_date`, `t`.`transaction_type` AS `transaction_type`, `e`.`equipment_name` AS `equipment_name`, `c`.`category_name` AS `category_name`, `t`.`quantity` AS `quantity`, `u`.`name` AS `handled_by_name`, `u`.`role` AS `handled_by_role`, CASE WHEN `t`.`used_by` is not null THEN `s`.`name` ELSE '-' END AS `student_name`, CASE WHEN `t`.`used_by` is not null THEN `s`.`nim` ELSE '-' END AS `student_nim`, `t`.`note` AS `note` FROM ((((`inventory_transaction` `t` join `laboratory_equipment` `e` on(`t`.`equipment_id` = `e`.`id`)) join `equipment_category` `c` on(`e`.`category_id` = `c`.`id`)) join `users` `u` on(`t`.`handled_by` = `u`.`id`)) left join `student` `s` on(`t`.`used_by` = `s`.`id`)) ORDER BY `t`.`transaction_date` DESC ;

-- --------------------------------------------------------

--
-- Struktur untuk view `user_list`
--
DROP TABLE IF EXISTS `user_list`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `user_list`  AS SELECT `users`.`id` AS `id`, `users`.`name` AS `name`, `users`.`email` AS `email`, `users`.`role` AS `role`, CASE WHEN `users`.`role` = 'ADMIN' THEN 'Administrator' WHEN `users`.`role` = 'PETUGAS' THEN 'Petugas Lab' END AS `role_display`, `users`.`created_at` AS `created_at` FROM `users` ORDER BY `users`.`role` ASC, `users`.`id` ASC ;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `equipment_category`
--
ALTER TABLE `equipment_category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indeks untuk tabel `equipment_condition_log`
--
ALTER TABLE `equipment_condition_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `checked_by` (`checked_by`),
  ADD KEY `idx_log_equipment` (`equipment_id`),
  ADD KEY `idx_log_date` (`check_date`);

--
-- Indeks untuk tabel `inventory_transaction`
--
ALTER TABLE `inventory_transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_transaction_equipment` (`equipment_id`),
  ADD KEY `idx_transaction_type` (`transaction_type`),
  ADD KEY `idx_transaction_date` (`transaction_date`),
  ADD KEY `idx_transaction_user` (`handled_by`),
  ADD KEY `idx_transaction_student` (`used_by`);

--
-- Indeks untuk tabel `laboratory_equipment`
--
ALTER TABLE `laboratory_equipment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_equipment_category` (`category_id`),
  ADD KEY `idx_equipment_name` (`equipment_name`),
  ADD KEY `idx_equipment_status` (`condition_status`);

--
-- Indeks untuk tabel `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nim` (`nim`),
  ADD KEY `idx_student_nim` (`nim`),
  ADD KEY `idx_nim_student` (`nim`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_user_email` (`email`),
  ADD KEY `idx_user_role` (`role`);

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `equipment_condition_log`
--
ALTER TABLE `equipment_condition_log`
  ADD CONSTRAINT `equipment_condition_log_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `laboratory_equipment` (`id`),
  ADD CONSTRAINT `equipment_condition_log_ibfk_2` FOREIGN KEY (`checked_by`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `inventory_transaction`
--
ALTER TABLE `inventory_transaction`
  ADD CONSTRAINT `inventory_transaction_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `laboratory_equipment` (`id`),
  ADD CONSTRAINT `inventory_transaction_ibfk_2` FOREIGN KEY (`handled_by`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `inventory_transaction_ibfk_3` FOREIGN KEY (`used_by`) REFERENCES `student` (`id`);

--
-- Ketidakleluasaan untuk tabel `laboratory_equipment`
--
ALTER TABLE `laboratory_equipment`
  ADD CONSTRAINT `laboratory_equipment_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `equipment_category` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
