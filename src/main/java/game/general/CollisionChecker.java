package game.general;

import game.entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        if (entity.direction[0].compareTo("up") == 0) {
            entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                entity.collisionOn[0] = true;
            }
        }
        entityTopRow = entityTopWorldY / gp.tileSize;
        if (entity.direction[1].compareTo("right") == 0) {
            entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                entity.collisionOn[1] = true;
            }
        }
        entityRightCol = entityRightWorldX / gp.tileSize;
        if (entity.direction[0].compareTo("down") == 0) {
            entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                entity.collisionOn[2] = true;
            }
        }
        entityBottomRow = entityBottomWorldY / gp.tileSize;
        if (entity.direction[1].compareTo("left") == 0) {
            entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                entity.collisionOn[3] = true;
            }
        }
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // get entity;s solid area location
                entity.solidArea.x = entity.worldX;
                entity.solidArea.y = entity.worldY;

                // get object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn[0] = true;
                    index = i;
                }
                entity.solidArea.y += entity.speed * 2;
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn[2] = true;
                    index = i;
                }
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn[3] = true;
                    index = i;
                }
                entity.solidArea.x += entity.speed * 2;
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn[1] = true;
                    index = i;
                }

                // resets the player and target's solidArea to their default values
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}