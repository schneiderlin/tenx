package com.example.linzihao97.plugindemo.inspection;

import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.siyeh.ig.psiutils.ExpressionUtils.isNullLiteral;

public class MultipleUpdateInspection extends AbstractBaseJavaLocalInspectionTool {

    public static final String QUICK_FIX_NAME = "SDK: " + InspectionsBundle.message("inspection.comparing.references.use.quickfix");

    @Override
    public ProblemDescriptor @Nullable [] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        Set<PsiMethod> visited = new HashSet<>();
        Optional<PsiCallExpression> recursion = findRecursion(method, visited, Set.of("aggregateUpdate"));

        return recursion.map(psiMethod -> new ProblemDescriptor[]{
                manager.createProblemDescriptor(
                        psiMethod,
                        "重复调用聚合根更新",
                        new CriQuickFix(),
                        ProblemHighlightType.GENERIC_ERROR,
                        isOnTheFly
                )
        }).orElse(null);
    }

    private void a() {
        b();
    }

    private void b() {
        a();
    }

    /**
     * 调用栈中是否有递归调用 `namePool` 中的函数
     *
     * @return 重复的的递归调用点
     */
    private static Optional<PsiCallExpression> findRecursion(PsiMethod method, Set<PsiMethod> visited, Set<String> namePool) {
        visited.add(method);
        Collection<PsiCallExpression> calls = PsiTreeUtil.findChildrenOfType(method.getBody(), PsiCallExpression.class);
        for (PsiCallExpression call : calls) {
            PsiMethod method1 = call.resolveMethod();
            Optional<PsiMethod> recur = visited.stream()
                    .filter(m -> namePool.contains(m.getName()))
                    .filter(m -> m.isEquivalentTo(method1))
                    .findAny();

            if (recur.isPresent()) {
                return Optional.of(call);
            }
            Optional<PsiCallExpression> r = findRecursion(method1, visited, namePool);
            if (r.isPresent()) {
                return r;
            }
        }

        return Optional.empty();
    }

    private static class CriQuickFix implements LocalQuickFix {

        @NotNull
        @Override
        public String getName() {
            return QUICK_FIX_NAME;
        }

        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }

    }
}
