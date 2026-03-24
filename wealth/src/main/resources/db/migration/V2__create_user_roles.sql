-- 创建角色表
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- 创建权限表
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- 插入默认角色
INSERT INTO roles (name, description) VALUES
('ADMIN', '系统管理员'),
('USER', '普通用户');

-- 插入默认权限
INSERT INTO permissions (code, name, description) VALUES
('USER_READ', '查看用户', '查看用户信息的权限'),
('USER_WRITE', '管理用户', '管理用户信息的权限'),
('CUSTOMER_READ', '查看客户', '查看客户信息的权限'),
('CUSTOMER_WRITE', '管理客户', '管理客户信息的权限'),
('ACCOUNT_READ', '查看账户', '查看账户信息的权限'),
('ACCOUNT_WRITE', '管理账户', '管理账户信息的权限');

-- 为管理员角色分配所有权限
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

-- 为普通用户分配基本权限
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 1), (2, 3), (2, 5);

-- 为默认用户分配角色
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1); -- 假设id为1的用户是管理员