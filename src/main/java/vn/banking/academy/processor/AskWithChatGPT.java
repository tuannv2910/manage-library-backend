package vn.banking.academy.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.PdfReader;
import okhttp3.*;
import org.springframework.data.util.Pair;
import vn.banking.academy.model.ConversationBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AskWithChatGPT {
    Gson gson = new Gson();

    /**
     * @param conversationId :id của phiên chat đấy
     * @param question       : đặt cho chatGPT
     */
    public String startQuestion(String conversationId, String question, String requirementsToken, String accessToken) {
        if (conversationId.isEmpty())
            return null;
        if (question.isEmpty())
            return null;
        List<String> datas;
        try {
            ConversationBody conversation = new ConversationBody(conversationId, question);
            String payload = gson.toJson(conversation);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, payload);
            Request request = new Request.Builder()
                    .url("https://chat.openai.com/backend-api/conversation")
                    .method("POST", body)
                    .addHeader("accept", "text/event-stream")
                    .addHeader("accept-language", "en-US,en;q=0.9,vi;q=0.8")
                    .addHeader("authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .addHeader("oai-device-id", "oai-did=2ca588c2-6439-4ca8-9234-2044f5e44d12; intercom-device-id-dgkjq2bp=769fc77b-dc98-42da-9fde-81c946c9e361; ajs_user_id=user-4cUmQw6PITwFV4URsS8jN3uI; ajs_anonymous_id=65dd9696-4e15-4aa0-b906-15f3c729f073; cf_clearance=89G5B1AA8js7qUqOF9kbeTQBnRRoAU0lvwm7EkWjszk-1711037805-1.0.1.1-..8ITWtR93Km.cR383O91zyjPNgmVOepbE7EqMvH211ROrypmVLQvKyzQg9avhZRnt38eao5LQrCcGGg.yFyyQ; __Host-next-auth.csrf-token=447e2e4cc7375ea5ed26b2e70c33d3ef2782adb1a08eae15b7246aef06468d84%7C4b3a8b5c33464495b506da1f029e6d7b47c35571670130fcbbce6c074572ade8; _cfuvid=heKZNa7.AbH6aYJMp15hkt7FLX4CjBw1npiERH28W1Y-1711119468845-0.0.1.1-604800000; cf_clearance=SN9sXq3ZpxvHbhAdkuFlCE6UcLAgS5VxLcQ6nmigNso-1711119470-1.0.1.1-Fl2aXDoGVD4L3heyLB.mj24viqarK8F0zYa8FuJZNeVO.SFJaPeeQIlWs4jePRU1y009qDJbWMVmMffYdz0Fpw; __cflb=0H28vVfF4aAyg2hkHEuhVVUPGkAFmYvkKB6pS6SgXwK; __cf_bm=FNrirYsB32pgcK0nMHE5V_DuAS1IgNmeapww9K3_Z8k-1711121786-1.0.1.1-95JDanRyAIZeYw4R8NLXP9bJOMPN.T5FF0pBy5aikN5At0_5.JfEwEJTF1henkSf2YIo5AqR73Z7EBKQIbr2xA; __Secure-next-auth.callback-url=https%3A%2F%2Fchat.openai.com; __Secure-next-auth.session-token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..l4xhnFv0eS5wMeFP.COJpdKgoO3jSpv_Iz6L_ZAA2E3QmHj7J34viR5OHZ7G3zNcdbBwzHQ63NACTilYKXctIymIOTYCfn0juzBomOCTLKA-qzms8QrPVeSQHKG1VxW-cV3eriKQRoOuckxPA7_R4AIMoz5WVEquRlPGrJv_AOgeIO2dK5FFM2SL3b8VcpaTVyk4WzhxJmBLRO79pr92wPc5izNJ2WYIfqcWCLsEudC9ku9BPXF3N69XDyML5EiiVOcqQvr4sJ3gSShFkS4grS3iH4ra_JEQl-MfU82O37G889am6634G6_Fo3jwkTtqDHvtyIDvJVmjex6pV3dViNCuQ9d7XVtMSPm5NTqs6j0JW5RnaKIWgauGPvibTC5QyRXWtMVcKv2vKP1fnbSrLTxR82ZgbzDs03ilixCAW-SOijVBHf5007xwpvlDsXG6X8WftriVUxo_2FkpVGwBuhQ_Ni-0i51BNz4tx1PHKG2Xqm96v1vVNMAnBJy03gvohK08RKFx0Zq_cWq9OdlAFBZuTSby4mKNtQP71S3vRMxeFpwIOnMdpuDGNrnGp12AMTuLKY7RSpeCOL9eB_2_2wQ-9hAX73YTtMabgn9Xg99COXv3eRBMUH8w9QZYZW7eWwKLwG0SmbOvuYrkqs9KBzBSpBjsGtbO1nypVXcbev6L3AmtMEmUB8GgC4XxtEBQgE5FEToMzigeuXtH4IYK3bdFJbxzBte0MgOz_w029l_gg4rJL6E1bLftEzY52iYKreHtav0CxTLpc35owU-STrDnw-zKCQncRTIA4UcL5rd09YRUbA9Mfi2FSOVyGG-PBCPfGF9wifQHkQv2wlAwW6_48HRIdyHMGmAiaFqzrjACyHICa_XeVJ10xBHmWOXm7oQ-ix_vCwZL18-kNzAALztB1gq8ZSMhMt-eeENoD7-W4jFn2DQsBYrJxdYcfTbZyEy2nS_901m6n271qA-CmeNDliB5WtFate5h1tUmpurru22E0BJVRzqPx72iwCKSTNkicc_y83CWKei3_oFY2p5Egdd3L_Noci7D0Y8LI3zne1nyh4RqD2cNOgN5dDPEwPJyfqOwlvKP5c8PkYH5wpuOedS1nOxkEBBwzOs7oHSBq14VsiGsbn3d9jT-mD70h-bqvwY5bELrjCojpj9dTfzvboB1FTyp8dTXnA-O2jRxRKnPz8DcZpyDxXQKZbH3I-ytbRpA3V3LqZGxb-wQvYrgOECi8WwJOIU8M1WNva1UZ_4RQoynOrcUinm1IBeUsHbDFwr8IR9lVIgSQGr6RHxcHooHpuZCiaayHuKHFW7wwvpf8sUZU-jwQqQBH5JjtKaZcYYWBy2mXshudPrH1jSnJNAJ0GYxI1by9R_lrcJLxPAlPJYx_TqfLZipEOI93g52h4lhEhHywhag4GvtKQ9lL5pUUsd_lvibrnH9wzOJOfEsZBrqasXKWs4dOTzpurSbrf4svoSpa3hDa_Wuk39Wz2LDPBSfTq3s6PeQg12qtvDnJAkJqASiswCj7cfHzwiA1MYA38hs1b5pEjeFix_WctZ7fjuuvOluFspCAI_Pa9rmHTYCdB4SwUOWGgsjhkX5i2xN7IASxtZUyVW3vKxXioExsx3EGQ5gRR5XW7YzkbnxXIu1XC2tp0nBQh6fQhWXMuMl3eCtjFFQOpl6EKOwgulP-cz9tlRt4DDybZ2mEsJaX1-SeqFSYzOQ_m1hnITUtMUAvvTKRrVc77UdstfDPnbCrn6RCjJ31NDKzbNZ0qPDTT6V3DbaDtvQnGXOR_umXEDtrd6cH2JgBHkvorkiv2s_v6RZtyaFTvqp2dg3VwwGXxly3GdqRGvAhuCAiBRPm0tkpZHMZ5HKvuuPbNdfI_VqWVMNaHkgm0dxAoEhT4YTwrrAsr4-nh6WoihLmTv2rdQxJBTS4iYdoeDzDAJ1GS6d7T5AgF9gm1ItG4YPxOhIqqQCT7mCLuf2-j5r_0puaSLUh4c97NQiJODHGeBo8amclF89ggtyJX9QoqTs_jboofKxcAkrdQr2GjhD99jg4oDxygF5anRdifCXjTZpOTCP2UFeVds5yF-6u-VfHoGGMCHxjj1akvKiKzDbvMI-mlaWvmNRfpcyhbjjxDJCt7wXsCjkzkqIOC1KExtWdwVWWdVJq0ChRbFf-vmOf6yDAGOxZi-ZpIyVh8Ld3SoNAztivB1FwHRfpjmqn2uE5n1D2QcmivRtO3BqiivRy2yvQJC8q_GSYFMC4KW2o0AjgHMAScm4ifYuNpLX5PV6va18pPc9w9AmDstnXXHXJBZS7O4_nCJgbMqDnz8wkpV3cn8k0nFl1mAagx9OGzBjtLEu1qDYmfMW0H0IMirWb37pR9DYlyn1GPRA9TlHaCuMULACqiH3DLoXn5R9GJZMcB7vYJBRlLX1wUZ7GGryDKJ5etCPyssEpx5NlUz_bBahlRgbPEM7hheOjkkEeJ8QV_XLfyl90c58bIBUqC5Kn_x6rDuHGvWyyMlS4nA_6ukioKKebnDSgcnov1k93ETdIf_V-Tvi8PtTDWg_AuVh_e6nECr4Jzm3BM8B77L32WZszQnzzlEMewhbgBxJHEiVExN1IL7dFQWB3yuNl8OipzE6p8stw8rFxaGCwbpoktptCZwRviBWgc5LzCr3CDYOXxpXdqZ1xZHwpnBd3aULFyah8uqcZT6xh_VMXpmJaALJnuFHLe466DXFlYYIgWxlrvc9SC9xoBoVSVm_KXdD1zMKN9FQrFGjz3c8LK0MUCAI5U9HREb7uvTviwRGKdUkmLnYqDR6MDfbt4Y3v5d-iduEhdk-oZRuO4FmPJRLq-NtLKE64LHY9WOA5F93zMDGthPruum_E4hFPpBvK7sJnrIJ9uEKLMxXUVT2p_9DeFlq8qXli-gL5MPUBeUReKUl_-ozhlwTPUjy9dEsO0bZyjSq_661qm5ByMIrdhTjjCvQ.-X9Mppi-bC3b26RMDCzyWg; intercom-session-dgkjq2bp=cnZkb2NnRlZqSjUyQTNtMHJRdlE3RFZYb3Q4L3RyYXFNblRZci83Um1aWm9oaEtyZjNXSDZJUi93bGtxd1FGWi0tRU0xeEVwYWdIMDZuWGNzeU1SbFNQdz09--6e84656f56144484b2de3668278f749eff7bc8aa; _uasid=\"Z0FBQUFBQmxfYWJvWnlXU3dkeEJ5ZWxyemFaSjJQZ080RW9rSjVmbFozVmJ2aXZNSWxNRGpBNkZzaHRSM285c0xLU1drOF82UFlMODhTdFRpTE1uekFKV01ya2lJckQyaXkyM1FYS3o2SGRtX3BpZDV6R3phQ01YempxTXIzSUNwY2tLeTJ3LVlXenNONmZXTFlUOVNqT3dPS0hUaHRJUC00bE1jLUxtNUVVYnZZOWVkWmw0Wl9URWpXMVNTbExGWlltV1c1NWM3MjYwS1dYemFpS1JYTXk1cnV1dGtzLVFVWjNORzlUTWhfY25NdElDaHdGVG92bThKOVFKMmFRd053cUJhZVNGbEw0UHJRdXNOVllMQV9RNjlBQXpka1hGOEN1N2g3QVVEdjQwZmhEaTN5Q0s4QmdYVUliQXdFN1loWDFQbTRwZEVyNElBREJfUXNjZ2V4TWIxXzY0VVVCZGxRPT0=\"; _umsid=\"Z0FBQUFBQmxfYWJvUW10MExaM2lwTGhPbTlBaVYyR0JUWVBPOFVXT1A5bWtqMXdfUm9uaGxSa2VDY1JUNHhIai1WenlCUUlyWm1aekYwSlVMOVc0ZU4wTXJwNDA5b0JtWURmeC1MSmJCRzF0Zms0OUFHSmNWTDlSTi01WWllR0NoZHUyLXhUbm5XQVF3RHZSVW9VdjBzWUQwZW9DUnJtZDJUelNDNU56cl9SN0ptcDlmVkNnTGhlb2xpZmo1UnBZT2QxVWtWc29hLWZrWmpoOTVvbjcxZUdncWxud2lpVkYycDR5aDJ4Sk1Bd3ZSTXAwQm03WDR0bz0=\"; _dd_s=rum=0&expire=1711123054822")
                    .addHeader("oai-language", "en-US")
                    .addHeader("openai-sentinel-chat-requirements-token", requirementsToken)
                    .addHeader("origin", "https://chat.openai.com")
                    .addHeader("referer", "https://chat.openai.com/c/" + conversationId)
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                    .addHeader("Cookie", "__cf_bm=W8YO_nS.Chx8y5_AlJ_j3sftHeL6w1YUMhXtk17q86w-1711122108-1.0.1.1-_SAWExsG0O4hrxOd3W8Rxs6VozPW8JLFFJyvA2KXlV_OJZ1oWwBLBezgThkXaithF1zkaZhe2RWQkPKaqP515w; _cfuvid=kGNDPdC1qHITAQJcfOGnwSCAC_aUONlKmnhhAiaDX0M-1711119178811-0.0.1.1-604800000; _uasid=\"Z0FBQUFBQmxfYWZxRVNJWTNFWHBHQjFOb1BrVWw3MWpKTGZkTjZFU3B2ODRJQUM0WXJqdFhoTGhpWTh4X3h0dHk4N3p6ajh6dVFsSDY2bjRGcjh6ZWZPVzZtdzQ5YTJOTzVSZWRGZ1FSbFdiVmhDNWlLYkhsNDB0T1hQbkZPWjJURkdMZF9FRjd3elpPM1FNSndvd3NMbzV6VFJuTGtUMVd4cHhJWXRoUmhZcUY5MDd1SXp2OEZFaXFZVnRPNjZZS2Etdm5BX1FZZUhyZ2Frd215ZE5JQmxBOThnSmpCVUZjZmhtdjdFYnZGM2VGZUpCOTFFOHdnZjRhTl9sbDRyY1Z5dERUYUZaeUNVVVVadDcxY2tKc2l3ZkVtWUhjdWNCandUbDlSREd4LVFNZUlZT1B4TzFXaFdsUnhHVHJfSFVRaWdNOXMwRW5NeEY3NGZWZXNlcFUwNDdoWjkzeEsxZC13PT0=\"; _umsid=\"Z0FBQUFBQmxfYWZxS3ZHMXBLdjhNMjVubVlVNDI1SGlEeldldEYzNjlxeU5VbUU5UE9PUExic0pjRmFaV044d1U3WXlQdktGTmt3cmRIT2plVmlWdlNuMUk5TVNOTlB6MGNQa0dySnNzSWhIeGpMTjZpN3p6MUc1UXNITXZYV29tRmVEbDFDcnpOTzlnLVJqYlVRU25KbmZJa05vMVFtM1JFZUlTSkozTC00OGhlVC1CNEFGX2dWd3o3d1BYNHFQUFRUcDN6WjNvSk1WZEJTNmRBREFCc3hzTmYyNHBhUUFMaUU2ZUVpdUR0MjBLdUV3YWtWdlNndz0=\"")
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            res = res.replace("data: [DONE]", "");
            datas = new ArrayList<>(Arrays.asList(res.split("data: ")));
            String lastResponse = datas.get(datas.size() - 1);
            JsonObject lastResponseObject = new Gson().fromJson(lastResponse, JsonObject.class);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "co loi tra ve";
    }

    public static void main(String[] args) throws Exception {
        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJ0dWFubnZwaDE3NjU1QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlfSwiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS9hdXRoIjp7InBvaWQiOiJvcmctcHVGdWJPWnpsYkZmMmluWEhIRkxVYm1lIiwidXNlcl9pZCI6InVzZXItNGNVbVF3NlBJVHdGVjRVUnNTOGpOM3VJIn0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJhdXRoMHw2M2Q3NTg2OGJjOTY3OGNlYzFkNDU1NTkiLCJhdWQiOlsiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS92MSIsImh0dHBzOi8vb3BlbmFpLm9wZW5haS5hdXRoMGFwcC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNzEyNTUwNTc5LCJleHAiOjE3MTM0MTQ1NzksImF6cCI6IlRkSkljYmUxNldvVEh0Tjk1bnl5d2g1RTR5T282SXRHIiwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSBtb2RlbC5yZWFkIG1vZGVsLnJlcXVlc3Qgb3JnYW5pemF0aW9uLnJlYWQgb3JnYW5pemF0aW9uLndyaXRlIG9mZmxpbmVfYWNjZXNzIn0.N3OVcUOdKeuuBVB4Et1mkOMONvyEe8S6YlNONJ0qUchzGAXnA9I2pnpVIKxywkqLv9qAGXcYVAavcAnIuGnPlest-QmwU4ijJ812fm5cbEmLG3IeJKd8taK4uuZ58YYob7mplXpv8YfDVSU6s-_ilItnQdsKk8BwA8L3AUB_nvLr5HsZoUtqY5Tk5jdT8fMLw-V9GOkUL135ThVSaxOOQb-UPAlwuBka41RLXXDoLzRmBGOb2878kbmm6aQmk4lqtDQWV_n0qrgDAeRrxLUdXnTJSOcBz8akZycL8htkAQl9ZUmTzM6gvSLXDeSyrDgCdj8_THbYWZH1gv5EPc85UQ";
        ChatRequirementsToken token = new ChatRequirementsToken();
        AskWithChatGPT chatGPT = new AskWithChatGPT();
        String conversationId = "cac08c2d-6b29-4d2f-b4ab-67ee997943f7";
        try {
            PdfReader reader = new PdfReader("C:\\Users\\Admin\\Downloads\\ton-tu-binh-phap.pdf");
            int pages = reader.getNumberOfPages();
            for (int i = 15; i <= pages; i++) {
                String questionText = "Nội dung của trang sách thứ  " + i + " : " + ReadPDFFile.contentPage(reader, i);
                Pair<String, Integer> generator1 = token.generator(accessToken, conversationId);
                chatGPT.startQuestion(conversationId, questionText, generator1.getFirst(), accessToken);
                System.out.println("question page >>> " + i + " success");
                Thread.sleep(10000);
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
