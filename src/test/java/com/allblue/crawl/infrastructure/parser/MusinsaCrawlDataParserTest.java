package com.allblue.crawl.infrastructure.parser;

import com.allblue.card.application.command.CardCreateCommand;
import com.allblue.card.application.command.ProductCreateCommand;
import com.allblue.card.domain.model.enums.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MusinsaCrawlDataParserTest {

    private MusinsaCrawlDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new MusinsaCrawlDataParser(new ObjectMapper());
    }

    @Test
    @DisplayName("supportedPlatform?Ä MUSINSAÎ•?Î∞òÌôò?úÎã§")
    void supportedPlatform() {
        assertThat(parser.supportedPlatform()).isEqualTo(Platform.MUSINSA);
    }

    @Nested
    @DisplayName("?§Ï†ú Î¨¥Ïã†???∞Ïù¥???åÏã±")
    class ParseRealData {

        private static final String RAW_DATA = """
                [
                  {
                    "id": "1475883068452566428",
                    "createdBy": { "id": "1229453110790000259" },
                    "contentType": "USER_SNAP",
                    "formatType": "POST",
                    "detail": {
                      "title": "",
                      "content": "#Í¥ëÍ≥† #Î¨¥Ïã†??#?¥Î∞ò?îÌ???#?§Îäò?òÏä§??,
                      "formatType": "POST"
                    },
                    "model": {
                      "gender": "WOMEN",
                      "age": null,
                      "height": 168,
                      "weight": 44,
                      "skinTone": "NONE"
                    },
                    "goods": [
                      {
                        "id": 1475883068452566500,
                        "isMatched": true,
                        "goodsPlatform": "MUSINSA",
                        "goodsNo": "5916242",
                        "options": [
                          { "id": 1475883068452566500, "depth": 1, "optionName": "M" }
                        ]
                      }
                    ],
                    "tags": [
                      {"name": "Í∞úÍ∞ïÎ£?}, {"name": "Í∞úÍ∞ïÏΩîÎîî"}, {"name": "Í¥ëÍ≥†"},
                      {"name": "Íæ∏ÏïàÍæ?}, {"name": "Î¨¥Ïã†??}, {"name": "?¥Î∞ò?îÌ???},
                      {"name": "?§Îäò?òÏä§??}, {"name": "Ï∂úÍ∑ºÎ£?}
                    ],
                    "medias": [
                      {
                        "id": 1475883068452566500,
                        "type": "IMAGE",
                        "path": "https://image.msscdn.net/thumbnails/snap/images/2026/02/25/73ffa7fff50d45d28375464b1d801dab.jpg",
                        "videoId": null
                      },
                      {
                        "id": 1475883068452566500,
                        "type": "IMAGE",
                        "path": "https://image.msscdn.net/thumbnails/snap/images/2026/02/25/b465276a555a4351b81f8131f4ea57fd.jpg",
                        "videoId": null
                      }
                    ],
                    "status": {
                      "snapDisplayStatus": "DISPLAY",
                      "snapAdminCheckStatus": "CHECKED"
                    },
                    "goods_detail_list": [
                      {
                        "goodsNo": "5916242",
                        "platform": "MUSINSA",
                        "goodsName": "?åÎ¶¨???§Ìä∏?ºÏù¥??Î°±Ïä¨Î¶¨Î∏å ?¥Î°ú ?∞ÏÖîÏ∏?Î≤ÑÍ±¥??,
                        "price": 32990,
                        "normalPrice": 56000,
                        "discountRate": 41,
                        "brandName": "?¥Î∞ò?îÌ???,
                        "imageUrl": "https://image.msscdn.net/thumbnails/images/goods_img/20260120/5916242/5916242_17701917627149_500.jpg",
                        "linkUrl": "https://www.musinsa.com/products/5916242",
                        "saleStat": "SALE"
                      }
                    ]
                  }
                ]
                """;

        @Test
        @DisplayName("snap 1Í±¥ÏùÑ CardCreateCommand 1Í±¥ÏúºÎ°??åÏã±?úÎã§")
        void parseSingleSnap() throws JsonProcessingException {
            List<CardCreateCommand> result = parser.parse(RAW_DATA);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("originIdÎ•??¨Î∞îÎ•¥Í≤å ?åÏã±?úÎã§")
        void parseOriginId() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.originId()).isEqualTo("1475883068452566428");
        }

        @Test
        @DisplayName("platform?Ä MUSINSAÎ°?Í≥†Ï†ï?úÎã§")
        void parsePlatform() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.platform()).isEqualTo(Platform.MUSINSA);
        }

        @Test
        @DisplayName("model??height, weightÎ•??åÏã±?úÎã§")
        void parseModelInfo() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.height()).isEqualTo(168);
            assertThat(command.weight()).isEqualTo(44);
        }

        @Test
        @DisplayName("tagsÎ•??ºÌëúÎ°?Íµ¨Î∂Ñ?òÏó¨ ?åÏã±?úÎã§")
        void parseTags() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.tags()).isEqualTo("Í∞úÍ∞ïÎ£?Í∞úÍ∞ïÏΩîÎîî,Í¥ëÍ≥†,Íæ∏ÏïàÍæ?Î¨¥Ïã†???¥Î∞ò?îÌ????§Îäò?òÏä§??Ï∂úÍ∑ºÎ£?);
        }

        @Test
        @DisplayName("goods_detail_list?êÏÑú ?ÅÌíà ?ïÎ≥¥Î•??åÏã±?úÎã§")
        void parseProduct() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            List<ProductCreateCommand> products = command.productCreateCommands();

            assertThat(products).hasSize(1);

            ProductCreateCommand product = products.get(0);
            assertThat(product.originId()).isEqualTo("5916242");
            assertThat(product.brand()).isEqualTo("?¥Î∞ò?îÌ???);
            assertThat(product.name()).isEqualTo("?åÎ¶¨???§Ìä∏?ºÏù¥??Î°±Ïä¨Î¶¨Î∏å ?¥Î°ú ?∞ÏÖîÏ∏?Î≤ÑÍ±¥??);
            assertThat(product.price()).isEqualTo(32990);
            assertThat(product.productUrl()).isEqualTo("https://www.musinsa.com/products/5916242");
        }

        @Test
        @DisplayName("goods??options?êÏÑú goodsNo Îß§Ïπ≠?ºÎ°ú ?µÏÖò???åÏã±?úÎã§")
        void parseProductOption() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.option()).isEqualTo("M");
        }

        @Test
        @DisplayName("goods??isMatchedÍ∞Ä true?¥Î©¥ isSimilar??false?¥Îã§")
        void parseIsSimilar() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.isSimilar()).isFalse();
        }
    }

    @Nested
    @DisplayName("?£Ï? ÏºÄ?¥Ïä§")
    class EdgeCases {

        @Test
        @DisplayName("idÍ∞Ä ?ÜÎäî snap?Ä Í±¥ÎÑà?¥Îã§")
        void skipSnapWithoutId() throws JsonProcessingException {
            String rawData = """
                    [{"model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            List<CardCreateCommand> result = parser.parse(rawData);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("tagsÍ∞Ä Îπ?Î∞∞Ïó¥?¥Î©¥ null??Î∞òÌôò?úÎã§")
        void emptyTags() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.tags()).isNull();
        }

        @Test
        @DisplayName("mediasÍ∞Ä ÎπÑÏñ¥?àÏúºÎ©?Ïπ¥Îìú ?¥Î?ÏßÄ originUrl?Ä null?¥Îã§")
        void emptyMedias() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.cardImage().originUrl()).isNull();
        }

        @Test
        @DisplayName("model ?ïÎ≥¥Í∞Ä ?ÜÏúºÎ©?height, weight??null?¥Îã§")
        void emptyModel() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.height()).isNull();
            assertThat(command.weight()).isNull();
        }

        @Test
        @DisplayName("?òÎ™ª??JSON?¥Î©¥ CrawlBusinessException???òÏßÑ??)
        void invalidJson() {
            assertThatThrownBy(() -> parser.parse("invalid json"))
                    .isInstanceOf(JsonProcessingException.class);
        }

        @Test
        @DisplayName("goods?êÏÑú isMatchedÍ∞Ä false?¥Î©¥ isSimilar??true?¥Îã§")
        void unmatchedGoodsIsSimilar() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"},
                      "goods": [{"goodsNo": "100", "isMatched": false, "options": []}],
                      "goods_detail_list": [{"goodsNo": "100", "goodsName": "?åÏä§??, "price": 1000,
                        "brandName": "Î∏åÎûú??, "imageUrl": "https://img.com/1.jpg", "linkUrl": "https://link.com"}]
                    }]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.isSimilar()).isTrue();
        }
    }
}
