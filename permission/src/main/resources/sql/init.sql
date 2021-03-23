
-- ----------------------------
-- Table structure for t_button
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_button";
CREATE TABLE "public"."t_button" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "label" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "index" int4 NOT NULL,
  "type" int2 NOT NULL
)
;

COMMENT ON COLUMN "public"."t_button"."name" IS '按钮名称';
COMMENT ON COLUMN "public"."t_button"."parent_id" IS '父按钮id';
COMMENT ON COLUMN "public"."t_button"."label" IS '按钮文本';
COMMENT ON COLUMN "public"."t_button"."index" IS '排序依据';
COMMENT ON COLUMN "public"."t_button"."type" IS ' 1-菜单/目录 2-tabs 3-按钮';

-- ----------------------------
-- Records of t_button
-- ----------------------------

-- ----------------------------
-- Table structure for t_button_interface
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_button_interface";
CREATE TABLE "public"."t_button_interface" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "button_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "interface_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."t_button_interface"."button_id" IS '按钮id';
COMMENT ON COLUMN "public"."t_button_interface"."interface_id" IS '接口id';

-- ----------------------------
-- Records of t_button_interface
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_interface";
CREATE TABLE "public"."t_interface" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "notes" varchar(128) COLLATE "pg_catalog"."default",
  "tag" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "url" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "method_type" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "is_authorized" bool NOT NULL,
  "is_public" bool NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."t_interface"."name" IS '接口名称';
COMMENT ON COLUMN "public"."t_interface"."notes" IS '接口描述';
COMMENT ON COLUMN "public"."t_interface"."tag" IS '接口标签';
COMMENT ON COLUMN "public"."t_interface"."url" IS '接口路径';
COMMENT ON COLUMN "public"."t_interface"."method_type" IS '请求类型';
COMMENT ON COLUMN "public"."t_interface"."is_authorized" IS '是否鉴权';
COMMENT ON COLUMN "public"."t_interface"."is_public" IS '是否开放接口';

-- ----------------------------
-- Records of t_interface
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_role";
CREATE TABLE "public"."t_role" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(128) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "create_user" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."t_role"."name" IS '角色名称';
COMMENT ON COLUMN "public"."t_role"."description" IS '角色描述';
COMMENT ON COLUMN "public"."t_role"."create_user" IS '创建者';
COMMENT ON COLUMN "public"."t_role"."update_user" IS '修改者';

-- ----------------------------
-- Records of t_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_role_button
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_role_button";
CREATE TABLE "public"."t_role_button" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "role_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "button_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."t_role_button"."role_id" IS '角色id';
COMMENT ON COLUMN "public"."t_role_button"."button_id" IS '按钮id';

-- ----------------------------
-- Records of t_role_button
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_user";
CREATE TABLE "public"."t_user" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(64) COLLATE "pg_catalog"."default",
  "email" varchar(64) COLLATE "pg_catalog"."default",
  "phone" varchar(16) COLLATE "pg_catalog"."default",
  "create_user" varchar(64) COLLATE "pg_catalog"."default",
  "update_user" varchar(64) COLLATE "pg_catalog"."default",
  "salt" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT now(),
  "update_time" timestamp(6) NOT NULL DEFAULT now(),
  "xzcode" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "is_enable" bool NOT NULL DEFAULT false,
  "level" int2 NOT NULL,
  "company" varchar(128) COLLATE "pg_catalog"."default",
)
;

CREATE UNIQUE INDEX "uk_name" ON "public"."t_user" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "public"."t_user"."name" IS '名称';
COMMENT ON COLUMN "public"."t_user"."description" IS '描述';
COMMENT ON COLUMN "public"."t_user"."create_user" IS '创建者';
COMMENT ON COLUMN "public"."t_user"."update_user" IS '修改者';
COMMENT ON COLUMN "public"."t_user"."salt" IS '盐';
COMMENT ON COLUMN "public"."t_user"."xzcode" IS '行政编码';
COMMENT ON COLUMN "public"."t_user"."is_enable" IS '是否可用';
COMMENT ON COLUMN "public"."t_user"."level" IS '用户级别，国家0 省1 市2 县3 乡4';
COMMENT ON COLUMN "public"."t_user"."company" IS '单位';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO "public"."t_user" VALUES ('1', 'admin', 'c9fc1bc923b0a1546a2c401f765ff777', '系统管理员', 'admin@test.com', NULL, '', NULL, '92eef3f801', '2020-02-19 11:06:36', '2020-02-19 11:09:30', '93', 't', 0, '管理员');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_user_role";
CREATE TABLE "public"."t_user_role" (
  "guid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "role_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."t_user_role"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."t_user_role"."role_id" IS '角色id';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------

-- ----------------------------
-- Uniques structure for table t_button
-- ----------------------------
ALTER TABLE "public"."t_button" ADD CONSTRAINT "uk_view_btn" UNIQUE ("name", "parent_id");
COMMENT ON CONSTRAINT "uk_view_btn" ON "public"."t_button" IS '每个页面每个按钮名称惟一';

-- ----------------------------
-- Primary Key structure for table t_button
-- ----------------------------
ALTER TABLE "public"."t_button" ADD CONSTRAINT "t_btn_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Primary Key structure for table t_button_interface
-- ----------------------------
ALTER TABLE "public"."t_button_interface" ADD CONSTRAINT "t_btn_interface_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Primary Key structure for table t_interface
-- ----------------------------
ALTER TABLE "public"."t_interface" ADD CONSTRAINT "t_interface_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Indexes structure for table t_role
-- ----------------------------
CREATE INDEX "idx_name" ON "public"."t_role" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_name" IS '角色名称唯一';

-- ----------------------------
-- Primary Key structure for table t_role
-- ----------------------------
ALTER TABLE "public"."t_role" ADD CONSTRAINT "t_role_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Primary Key structure for table t_role_button
-- ----------------------------
ALTER TABLE "public"."t_role_button" ADD CONSTRAINT "t_role_btn_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Indexes structure for table t_user
-- ----------------------------
CREATE INDEX "uk_name" ON "public"."t_user" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "public"."t_user" ADD CONSTRAINT "t_user_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Primary Key structure for table t_user_role
-- ----------------------------
ALTER TABLE "public"."t_user_role" ADD CONSTRAINT "t_user_role_pkey" PRIMARY KEY ("guid");

-- ----------------------------
-- Foreign Keys structure for table t_button_interface
-- ----------------------------
ALTER TABLE "public"."t_button_interface" ADD CONSTRAINT "button" FOREIGN KEY ("button_id") REFERENCES "public"."t_button" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."t_button_interface" ADD CONSTRAINT "interface" FOREIGN KEY ("interface_id") REFERENCES "public"."t_interface" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table t_role_button
-- ----------------------------
ALTER TABLE "public"."t_role_button" ADD CONSTRAINT "button" FOREIGN KEY ("button_id") REFERENCES "public"."t_button" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."t_role_button" ADD CONSTRAINT "role" FOREIGN KEY ("role_id") REFERENCES "public"."t_role" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table t_user_role
-- ----------------------------
ALTER TABLE "public"."t_user_role" ADD CONSTRAINT "role" FOREIGN KEY ("role_id") REFERENCES "public"."t_role" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."t_user_role" ADD CONSTRAINT "user" FOREIGN KEY ("user_id") REFERENCES "public"."t_user" ("guid") ON DELETE NO ACTION ON UPDATE NO ACTION;
